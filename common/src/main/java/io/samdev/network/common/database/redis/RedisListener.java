package io.samdev.network.common.database.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.samdev.network.common.database.redis.listener.RedisHandler;
import io.samdev.network.common.database.redis.listener.RedisMethodValidator;
import io.samdev.network.common.database.redis.serialize.PacketDeserializer;
import io.samdev.network.common.error.ErrorHandler;
import io.samdev.network.common.util.Logging;
import io.samdev.network.common.util.UtilJson;
import lombok.Value;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used by {@link RedisManager} for
 * listening to incoming data across channels
 */
class RedisListener extends JedisPubSub
{
    /**
     * Map linking {@link NetworkPacket} types
     * to sets of {@link MethodData} objects
     */
    private final Map<Class<? extends NetworkPacket>, Set<MethodData>> methodMap = new HashMap<>();

    /**
     * Class for storing data about a Redis
     * listening method and its source object
     */
    @Value
    private static class MethodData
    {
        private final Method method;
        private final Object target;
        private final Set<String> channels;
    }

    /**
     * Registers an object for Redis listening
     *
     * @param object The object
     */
    @SuppressWarnings("unchecked")
    void registerListener(Object object)
    {
        for (Method method : object.getClass().getDeclaredMethods())
        {
            if (methodValidator.isValidMethod(method))
            {
                Class<? extends NetworkPacket> packetClass = (Class<? extends NetworkPacket>) method.getParameterTypes()[0];

                methodMap.computeIfAbsent(packetClass, v -> new HashSet<>()).add(constructMethodData(method, object));
            }
        }
    }

    /**
     * Constructs a {@link MethodData}
     * object for a Redis listening method
     *
     * @param method The method
     * @param object The object instance
     *
     * @return The constructed {@link MethodData} object
     */
    private MethodData constructMethodData(Method method, Object object)
    {
        Set<String> channels = new HashSet<>(
            Arrays.asList(method.getAnnotation(RedisHandler.class).channels())
        );

        return new MethodData(method, object, channels);
    }

    /**
     * The {@link RedisMethodValidator} instance
     */
    private final RedisMethodValidator methodValidator = new RedisMethodValidator();

    /**
     * The {@link PacketDeserializer} instance
     */
    private final PacketDeserializer packetDeserializer = new PacketDeserializer();

    @Override
    public void onMessage(String channel, String message)
    {
        JsonObject json;

        try
        {
            json = UtilJson.parseObject(message);
        }
        catch (JsonSyntaxException ex)
        {
            Logging.severe("Invalid JSON received from redis: \n" + message);
            return;
        }

        handleJson(channel, json);
    }

    /**
     * Handles an incoming
     * {@link JsonObject} from Redis
     *
     * @param channel The channel the data was received from
     * @param json The JSON data
     */
    @SuppressWarnings("unchecked")
    private void handleJson(String channel, JsonObject json)
    {
        try
        {
            NetworkPacket packet = packetDeserializer.deserialize(json);
            handlePacket(channel, packet);
        }
        catch (ClassNotFoundException ex)
        {
            Logging.severe("Could not find class from Redis packet: " + json.get("class").getAsString());
            ErrorHandler.report(ex);
        }
    }

    /**
     * Handles the receiving and
     * event dispensing of a received
     * {@link NetworkPacket} over Redis
     *
     * @param channel The channel the packet was received from
     * @param packet The received packet
     */
    private void handlePacket(String channel, NetworkPacket packet)
    {
        Set<MethodData> methodDatas = methodMap.get(packet.getClass());

        if (methodDatas == null)
        {
            return;
        }

        methodDatas.forEach(data ->
        {
            if (data.getChannels().contains(channel))
            {
                try
                {
                    data.getMethod().invoke(data.getTarget(), packet);
                }
                catch (IllegalAccessException | InvocationTargetException ex)
                {
                    Logging.severe("Error executing Redis listening method");
                    ErrorHandler.report(ex);
                }
            }
        });
    }
}
