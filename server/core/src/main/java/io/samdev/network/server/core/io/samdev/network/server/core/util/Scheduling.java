package io.samdev.network.server.core.io.samdev.network.server.core.util;

import io.samdev.network.common.error.ErrorHandler;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Utilities involving the {@link BukkitScheduler}
 */
@UtilityClass
public final class Scheduling
{
    /**
     * Executes a task on the server's main thread
     *
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     *
     * @param runnable The runnable to execute
     */
    public static void sync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(UtilServer.getPlugin(), runnable);
    }

    /**
     * Executes a task asynchronously
     *
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     *
     * @param runnable The runnable to execute
     */
    public static void async(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(UtilServer.getPlugin(), runnable);
    }

    /**
     * Checks that a section of potentially
     * blocking code is not running on the
     * server's main thread
     *
     * @see Bukkit#isPrimaryThread()
     *
     * @throws RuntimeException If the current thread is the server's main thread
     */
    public static void mainThreadCheck()
    {
        if (Bukkit.isPrimaryThread())
        {
            ErrorHandler.report(new RuntimeException("Server attempted to perform blocking operation on main thread"));
        }
    }
}
