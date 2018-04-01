package io.samdev.network.common.database.redis.listener;

import io.samdev.network.common.database.redis.RedisManager;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used to denote that a
 * method should be used for listening
 * to Redis packets
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RedisHandler
{
    /**
     * The channels to listen to
     *
     * Defaults to {@link RedisManager#GLOBAL_CHANNEL}
     */
    String[] channels() default {RedisManager.GLOBAL_CHANNEL};
}
