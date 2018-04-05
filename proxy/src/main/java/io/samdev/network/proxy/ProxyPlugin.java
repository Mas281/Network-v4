package io.samdev.network.proxy;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.samdev.network.common.database.redis.RedisManager;
import io.samdev.network.common.util.Logging;
import io.samdev.network.common.util.Logging.WrappingLogProvider;
import io.samdev.network.common.util.UtilJson;
import io.samdev.network.proxy.listener.PingListener;
import io.samdev.network.proxy.util.UtilProxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Main class for the Proxy plugin
 * which will run on the BungeeCord instance
 */
public class ProxyPlugin extends Plugin
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
    private RedisManager redisManager;

    @Override
    public void onEnable()
    {
        Logging.setLogProvider(new WrappingLogProvider(getLogger()));

        injector = Guice.createInjector(binder ->
        {
           this.binder = binder;

           bindInstance(ProxyPlugin.class, this);
           binder.requestStaticInjection(UtilProxy.class);

           bindInstance(ProxySettings.class, UtilJson.parseConfigSection(ProxySettings.class, "proxy_settings"));

           bindInstance(ProxyServer.class, getProxy());

           bindInstance(RedisManager.class, new RedisManager());
        });

        injector.injectMembers(this);

        // Connect to services
        redisManager.connect();

        // Register modules
        register(PingListener.class);
    }

    /**
     * Creates an instance of a {@link Class}
     * with Guice injection, and registers
     * the class as a Bungee {@link Listener}
     * if applicable
     *
     * @param clazz The class
     */
    private <T> void register(Class<T> clazz)
    {
        T object = injector.getInstance(clazz);
        UtilProxy.registerListenerSafe(object);
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
        UtilProxy.registerListenerSafe(instance);
    }
}
