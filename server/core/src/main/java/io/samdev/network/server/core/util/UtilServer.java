package io.samdev.network.server.core.util;

import com.google.inject.Inject;
import io.samdev.network.server.core.CorePlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Spigot {@link Server} utilities
 */
@UtilityClass
public final class UtilServer
{
    /**
     * The {@link CorePlugin} instance to be
     * injected upon plugin startup
     */
    @Inject
    private static CorePlugin PLUGIN;

    /**
     * Fetches the core plugin instance
     *
     * @see #PLUGIN
     *
     * @return The core plugin instance
     */
    static CorePlugin getPlugin()
    {
        return PLUGIN;
    }

    /**
     * Registers a Bukkit {@link Listener}
     *
     * @see PluginManager#registerEvents(Listener, Plugin)
     *
     * @param listener The listener
     */
    public static void registerListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }

    /**
     * Registers an {@link Object}
     * as a Bukkit {@link Listener} if it
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
