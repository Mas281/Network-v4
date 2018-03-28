package io.samdev.network.common.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import io.samdev.network.common.database.NetworkDatabase;
import io.samdev.network.common.player.NetworkUser;
import io.samdev.network.common.util.Paths;
import io.samdev.network.common.util.UtilJson;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.dao.DAO;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MongoDB implementation of {@link NetworkDatabase}
 */
public class MongoDatabase implements NetworkDatabase
{
    private final MongoCredentials credentials;
    private final MongoClientOptions options;

    public MongoDatabase()
    {
        this(UtilJson.fromJson(
                MongoCredentials.class,
                UtilJson.parse(Paths.NETWORK_CONFIG).get("mongodb")
            ),
            MongoClientOptions.builder().build()
        );
    }

    /**
     * Constructor for a {@link MongoDatabase}
     *
     * @param credentials The MongoDB credentials
     */
    public MongoDatabase(MongoCredentials credentials, MongoClientOptions options)
    {
        this.credentials = checkNotNull(credentials);
        this.options = checkNotNull(options);
    }

    /**
     * The {@link MongoClient} instance
     */
    private MongoClient mongoClient;

    /**
     * The {@link Datastore} instance
     */
    private Datastore datastore;

    /**
     * The {@link UserDao} instance
     */
    private DAO<NetworkUser, String> userDao;

    @Override
    public void connect()
    {
        checkArgument(!isConnected(), "Client is already connected");

        mongoClient = new MongoClient(credentials.createAddress(), credentials.createCredential(), options);

        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setObjectFactory(new NetworkObjectFactory());

        morphia.map(NetworkUser.class);

        // Disables Mongo's spammy connection logging
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        datastore = morphia.createDatastore(mongoClient, credentials.getDatabase());
        datastore.ensureIndexes();

        userDao = new UserDao(datastore);
    }

    @Override
    public void disconnect()
    {
        checkArgument(isConnected(), "Client is not connected");

        mongoClient.close();
    }

    @Override
    public boolean isConnected()
    {
        return mongoClient != null;
    }

    /**
     * {@link BasicDAO} wrapper for storing {@link NetworkUser} objects
     */
    private class UserDao extends BasicDAO<NetworkUser, String>
    {
        public UserDao(Datastore datastore)
        {
            super(NetworkUser.class, datastore);
        }
    }
}
