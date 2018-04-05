package io.samdev.network.proxy.util;

import com.google.inject.Inject;
import io.samdev.network.proxy.ProxyPlugin;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * Proxy server utilities
 */
@UtilityClass
public final class UtilProxy
{
    /**
     * The {@link ProxyPlugin} instance
     * to be injected upon proxy startup
     */
    @Inject
    private static ProxyPlugin PLUGIN;

    /**
     * Registers a Bungee {@link Listener}
     *
     * @see PluginManager#registerListener(Plugin, Listener)
     *
     * @param listener The listener
     */
    public static void registerListener(Listener listener)
    {
        ProxyServer.getInstance().getPluginManager().registerListener(PLUGIN, listener);
    }

    /**
     * Registers an {@link Object}
     * as a Bungee {@link Listener} if it
     * is a valid Listener instance
     *
     * @see #registerListener(Listener)
     *
     * @param object The object
     */
    public static void registerListenerSafe(Object object)
    {
        if (object instanceof Listener)
        {
            registerListener((Listener) object);
        }
    }
}
