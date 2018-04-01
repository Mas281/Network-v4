package io.samdev.network.common.database.redis.serialize;

import com.google.gson.JsonObject;
import io.samdev.network.common.database.redis.NetworkPacket;
import io.samdev.network.common.util.UtilJson;

/**
 * Class used to deserialize JSON objects from
 * Redis into {@link NetworkPacket} objects
 */
public class PacketDeserializer
{
    /**
     * Deserializes a JSON object
     * into a {@link NetworkPacket} object
     *
     * @param json The JSON object
     */
    @SuppressWarnings("unchecked")
    public NetworkPacket deserialize(JsonObject json) throws ClassNotFoundException
    {
        String clazzName = json.get("class").getAsString();

        Class<? extends NetworkPacket> clazz = (Class<? extends NetworkPacket>) Class.forName(clazzName);
        JsonObject data = json.getAsJsonObject("data");

        return UtilJson.fromJson(clazz, data);
    }
}