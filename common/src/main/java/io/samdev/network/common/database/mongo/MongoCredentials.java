package io.samdev.network.common.database.mongo;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import io.samdev.network.common.database.Credentials;
import lombok.Value;

/**
 * Class holding MongoDB credentials
 */
@Value
public class MongoCredentials implements Credentials
{
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    /**
     * Creates a {@link ServerAddress}
     * object from the given host
     * and port data
     *
     * @see #host
     * @see #port
     *
     * @return The {@link ServerAddress}
     */
    public ServerAddress createAddress()
    {
        return new ServerAddress(host, port);
    }

    /**
     * Creates a {@link MongoCredential}
     * object from the given username,
     * database, and password
     *
     * @see #database
     * @see #username
     * @see #password
     *
     * @return The {@link MongoCredential} object
     */
    public MongoCredential createCredential()
    {
        return MongoCredential.createCredential(username, database, password.toCharArray());
    }
}
