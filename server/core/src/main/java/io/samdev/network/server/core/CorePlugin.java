package io.samdev.network.server.core;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.samdev.network.common.database.NetworkDatabase;
import io.samdev.network.common.database.mongo.MongoDatabase;
import io.samdev.network.common.database.redis.RedisManager;
import io.samdev.network.common.server.ServerData;
import io.samdev.network.common.util.Logging;
import io.samdev.network.common.util.Logging.WrappingLogProvider;
import io.samdev.network.common.util.Paths;
import io.samdev.network.common.util.UtilJson;
import io.samdev.network.server.core.player.UserManager;
import io.samdev.network.server.core.player.listener.LoginHandler;
import io.samdev.network.server.core.util.UtilServer;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class for the Spigot server
 * core plugin, which provides utilities
 * and services for other plugins to use
 */
public class CorePlugin extends JavaPlugin
{
    /**
     * Google Guice dependency {@link Injector}
     */
    private Injector injector;

    /**
     * {@link Binder} instance for the {@link Injector}
     *
     * @see #injector
     */
    private Binder binder;

    @Inject
    private NetworkDatabase database;

    @Inject
    private RedisManager redisManager;

    @Override
    public void onEnable()
    {
        Logging.setLogProvider(new WrappingLogProvider(getLogger()));

        injector = Guice.createInjector(binder ->
        {
            this.binder = binder;

            // Bind required class dependencies
            bindInstance(CorePlugin.class, this);
            binder.requestStaticInjection(UtilServer.class);

            bindInstance(Server.class, getServer());

            bindInstance(ServerData.class,
                UtilJson.fromFile(ServerData.class, new File(Paths.SERVER_CONFIG_NAME))
            );

            bindInstance(NetworkDatabase.class, new MongoDatabase());
            bindInstance(RedisManager.class, new RedisManager());

            bindInstance(UserManager.class, new UserManager());
        });

        injector.injectMembers(this);

        // Connect to services
        database.connect();
        redisManager.connect();

        // Register modules
        register(LoginHandler.class);
    }

    /**
     * Creates an instance of a {@link Class}
     * with Guice injection, and registers
     * the class as a Bukkit {@link Listener}
     * if applicable
     *
     * @param clazz The class
     */
    private <T> void register(Class<T> clazz)
    {
        T object = injector.getInstance(clazz);
        UtilServer.registerListenerSafe(object);
    }

    /**
     * Binds an {@link Class} to
     * a specific instance using
     * the {@link Binder}
     *
     * @see #binder
     *
     * @param clazz The class
     * @param <T> The class type
     * @param instance The instance of the class
     */
    private <T> void bindInstance(Class<T> clazz, T instance)
    {
        binder.bind(clazz).toInstance(instance);
        UtilServer.registerListenerSafe(instance);
    }
}