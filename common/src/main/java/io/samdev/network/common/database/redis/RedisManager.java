package io.samdev.network.common.database.redis;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.samdev.network.common.database.Connectable;
import io.samdev.network.common.database.redis.serialize.PacketSerializer;
import io.samdev.network.common.util.UtilJson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class for managing the listening/publishing
 * of data through Redis channels, and also
 * the caching of any temporary data on the network
 */
public class RedisManager implements Connectable
{
    /**
     * The global channel which all {@link RedisManager}
     * instances will listen to incoming packets from
     */
    public static final String GLOBAL_CHANNEL = "global";

    private final RedisCredentials credentials;
    private final JedisPoolConfig poolConfig;

    public RedisManager()
    {
        this(
            UtilJson.parseCredentials(RedisCredentials.class, "redis"),
            new JedisPoolConfig()
        );
    }

    /**
     * Constructor for a {@link RedisManager}
     *
     * @param credentials The Redis credentials
     */
    public RedisManager(RedisCredentials credentials, JedisPoolConfig poolConfig)
    {
        this.credentials = credentials;
        this.poolConfig = poolConfig;
    }

    /**
     * The Jedis connection pool
     */
    private JedisPool pool;

    /**
     * The executor to use for async operations
     */
    private ExecutorService executor;

    /**
     * The {@link RedisListener} instance
     */
    private volatile RedisListener listener;

    /**
     * Thread-safe {@link Jedis} instance
     * for subscribing to Redis channels
     */
    private volatile Jedis jedisSubscriber;

    /**
     * Executor for subscribing to Jedis channels
     */
    private ExecutorService subscriberExecutor;

    /**
     * The {@link PacketSerializer} instance
     */
    private PacketSerializer packetSerializer;

    @Override
    public void connect()
    {
        checkArgument(!isConnected(), "Connection already established");

        // Connecting
        pool = new JedisPool(
            poolConfig, credentials.getHost(), credentials.getPort(), Protocol.DEFAULT_TIMEOUT, credentials.getPassword()
        );

        // Async operations
        executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder().setNameFormat("Redis Thread %d").build()
        );

        // Serialization
        packetSerializer = new PacketSerializer();

        // Subscribing
        listener = new RedisListener();
        jedisSubscriber = pool.getResource();

        subscriberExecutor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder().setNameFormat("Jedis Subscriber Thread").build()
        );

        subscribeChannel(GLOBAL_CHANNEL);
    }

    @Override
    public void disconnect()
    {
        checkArgument(isConnected(), "No connection active");

        // jedisSubscriber.close();

        pool.close();
        pool = null;
    }

    @Override
    public boolean isConnected()
    {
        return pool != null && !pool.isClosed();
    }

    /**
     * @see RedisListener#registerListener(Object)
     */
    public void registerListener(Object object)
    {
        listener.registerListener(object);
    }

    /**
     * Subscribes the {@link RedisListener}
     * to a new channel
     *
     * The listener is subscribed to the
     * global channel automatically
     *
     * @see #listener
     * @see #GLOBAL_CHANNEL
     *
     * @param channel The channel
     */
    public void subscribeChannel(String channel)
    {
        checkNotNull(listener, "Listener is null");

        subscriberExecutor.execute(() ->
            jedisSubscriber.subscribe(listener, channel)
        );
    }

    /**
     * Broadcasts a {@link NetworkPacket} across the global channel
     *
     * @see #GLOBAL_CHANNEL
     * @see #sendPacket(String, NetworkPacket)
     *
     * @param packet The packet
     */
    public void broadcastPacket(NetworkPacket packet)
    {
        sendPacket(GLOBAL_CHANNEL, packet);
    }

    /**
     * Sends a packet across the desired channel
     *
     * @param channel The channel
     * @param packet The packet
     */
    public void sendPacket(String channel, NetworkPacket packet)
    {
        executeJedisAsync(jedis ->
            jedis.publish(channel, packetSerializer.serialize(packet))
        );
    }

    /**
     * Executes an action asynchronously using
     * a {@link Jedis} instance from the pool
     *
     * @see #pool
     * @see #executeJedis(Consumer)
     *
     * @param consumer The action to execute
     */
    private void executeJedisAsync(Consumer<Jedis> consumer)
    {
        executor.submit(() -> executeJedis(consumer));
    }

    /**
     * Executes an action using a
     * {@link Jedis} instance from the pool
     *
     * @see #pool
     *
     * @param consumer The action to execute
     */
    private void executeJedis(Consumer<Jedis> consumer)
    {
        try (Jedis jedis = pool.getResource())
        {
            consumer.accept(jedis);
        }
    }
}