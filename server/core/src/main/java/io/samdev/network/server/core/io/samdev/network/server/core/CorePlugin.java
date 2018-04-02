package io.samdev.network.server.core.io.samdev.network.server.core;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.samdev.network.common.database.NetworkDatabase;
import io.samdev.network.common.database.mongo.MongoDatabase;
import io.samdev.network.common.database.redis.RedisManager;
import io.samdev.network.common.server.ServerData;
import io.samdev.network.common.util.Paths;
import io.samdev.network.common.util.UtilJson;
import io.samdev.network.server.core.io.samdev.network.server.core.player.UserManager;
import io.samdev.network.server.core.io.samdev.network.server.core.player.listener.LoginHandler;
import io.samdev.network.server.core.io.samdev.network.server.core.util.UtilServer;
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

    @Override
    public void onEnable()
    {
        injector = Guice.createInjector(binder ->
        {
            this.binder = binder;

            bindInstance(CorePlugin.class, this);
            binder.requestStaticInjection(UtilServer.class);

            bindInstance(ServerData.class,
                UtilJson.fromFile(ServerData.class, new File(Paths.SERVER_CONFIG_NAME))
            );

            bindInstance(NetworkDatabase.class, new MongoDatabase());
            bindInstance(RedisManager.class, new RedisManager());

            binder.bind(UserManager.class).toInstance(new UserManager());
            bindInstance(LoginHandler.class, new LoginHandler());
        });
    }

    /**
     * Injects bound class instances
     * in to an object
     *
     * @see #injector
     *
     * @param object The object
     */
    public void inject(Object object)
    {
        injector.injectMembers(object);
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

    private <T> void get(Class<T> clazz)
    {
        T object = injector.getInstance(clazz);

        if (object instanceof Listener)
        {
            UtilServer.registerListener((Listener) object);
        }
    }
}