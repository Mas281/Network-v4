package io.samdev.network.server.core.io.samdev.network.server.core.player.listener;

import com.google.inject.Inject;
import io.samdev.network.common.database.NetworkDatabase;
import io.samdev.network.common.database.redis.RedisManager;
import io.samdev.network.common.error.ErrorHandler;
import io.samdev.network.common.player.NetworkUser;
import io.samdev.network.common.server.ServerData;
import io.samdev.network.server.core.io.samdev.network.server.core.player.UserManager;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class handling the joining
 */
@RequiredArgsConstructor
public class LoginHandler implements Listener
{
    @Inject
    private UserManager userManager;

    @Inject
    private NetworkDatabase database;

    @Inject
    private RedisManager redisManager;

    @Inject
    private ServerData serverData;

    /**
     * The maximum time to attempt
     * to fetch a player's data before
     * timing out
     */
    private final Duration loadTimeout = Duration.ofSeconds(2L);

    /**
     * Handles an {@link AsyncPlayerPreLoginEvent}
     *
     * @param event The event
     */
    @EventHandler
    public void handleLogin(AsyncPlayerPreLoginEvent event)
    {
        UUID id = event.getUniqueId();
        NetworkUser user = null;

        boolean error = false;
        String kickMessage = ChatColor.RED + "";

        try
        {
            user = database.fetchUser(id)
                .get(loadTimeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException | ExecutionException ex)
        {
            kickMessage += "An error occured while loading your data";
            error = true;

            ErrorHandler.report(ex);
        }
        catch (TimeoutException ex)
        {
            kickMessage += "It took too long to load your data, try again";
            error = true;
        }

        if (user == null)
        {
            if (error)
            {
                // Deny the login because of an exception
                event.disallow(Result.KICK_OTHER, kickMessage);
                return;
            }

            // New user, create and insert
            // a new record for them
            user = createNewUser(event);
        }

        if (!user.hasRank(serverData.getRequiredRank()))
        {
            event.disallow(
                Result.KICK_WHITELIST, ChatColor.RED + "You don't have the required rank to join this server"
            );
            return;
        }

        userManager.addUser(user);
    }

    /**
     * Creates a new user from an {@link AsyncPlayerPreLoginEvent}
     * and inserts them into the database
     *
     * @param event The login event
     *
     * @return The new user
     */
    private NetworkUser createNewUser(AsyncPlayerPreLoginEvent event)
    {
        NetworkUser user = new NetworkUser(event.getUniqueId(), event.getName());
        database.insertUser(user);

        return user;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        userManager.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        userManager.removeUser(event.getPlayer());
    }
}
