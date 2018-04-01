package io.samdev.network.common.database.redis.listener;

import io.samdev.network.common.database.redis.NetworkPacket;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Class to determine whether class methods
 * are valid Redis listening methods
 */
public class RedisMethodValidator
{
    /**
     * Determines whether a method
     * is valid for Redis listening
     *
     * @param method The method
     *
     * @return Whether the method is valid
     */
    public boolean isValidMethod(Method method)
    {
        return
            method.getAnnotation(RedisHandler.class) != null &&
            method.getParameterCount() == 1 &&
            Modifier.isPublic(method.getModifiers()) &&
            NetworkPacket.class.isAssignableFrom(method.getParameterTypes()[0]);
    }
}
