package io.samdev.network.common.database.redis;

import java.time.Instant;

public class TestPacket implements NetworkPacket
{
    private final String stringValue = "Hello World";
    private final int intValue = 12345;
    private final Instant instantValue = Instant.ofEpochMilli(1514764800000L);
}
