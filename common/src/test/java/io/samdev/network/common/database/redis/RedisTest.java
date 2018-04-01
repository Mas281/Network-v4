package io.samdev.network.common.database.redis;

import io.samdev.network.common.database.redis.listener.RedisHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RedisTest
{
    private static RedisManager redis;

    @BeforeAll
    public static void connectToRedis()
    {
        redis = new RedisManager();
        redis.connect();
    }

    @Test
    public void testConnectionStatusTrueWhenConnected()
    {
        assertTrue(redis.isConnected(), "Redis is not connected");
    }

    private volatile boolean packetReceived = false;

    @Test
    public void testPacketReceivedWhenDispatched()
    {
        redis.registerListener(new Object()
        {
            @RedisHandler
            public void onTestPacketReceive(TestPacket packet)
            {
                packetReceived = true;
            }
        });

        redis.broadcastPacket(new TestPacket());
        long start = System.currentTimeMillis();

        while (!packetReceived)
        {
            if (System.currentTimeMillis() - start >= 5_000L)
            {
                fail("Redis packet failed to be received after 5s");
            }
        }
    }

    @AfterAll
    public static void disconnectFromRedis()
    {
        redis.disconnect();
    }
}
