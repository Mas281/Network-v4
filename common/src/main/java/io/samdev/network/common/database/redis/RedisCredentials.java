package io.samdev.network.common.database.redis;

import io.samdev.network.common.database.Credentials;
import lombok.Value;

/**
 * Class holding Redis credentials
 */
@Value
public class RedisCredentials implements Credentials
{
    private final String host;
    private final int port;
    private final String password;
}
