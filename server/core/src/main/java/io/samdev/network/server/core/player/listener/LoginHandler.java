package io.samdev.network.server.core.player.listener;

import com.google.inject.Inject;
import io.samdev.network.common.database.NetworkDatabase;
import io.samdev.network.common.database.redis.RedisManager;
import io.samdev.network.common.error.ErrorHandler;
import io.samdev.network.common.player.NetworkUser;
import io.samdev.network.common.server.ServerData;
import io.samdev.network.server.core.player.UserManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class handling the joining
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginHandler implements Listener
{
    @Inject
    private final UserManager userManager;

    @Inject
    private final NetworkDatabase database;

    @Inject
    private final RedisManager redisManager;

    @Inject
    private final ServerData serverData;

    /**
     * The maximum time to attempt
     * to fetch a player's data before
     * timing out
     */
    private Duration loadTimeout = Duration.ofSeconds(2L);

    /**
     * The message to kick players
     * with when they do not
     * have the required rank to
     * join the server
     */
    private String whitelistMessage = ChatColor.RED + "You aren't allowed to join this server";

    /**
     * Handles an {@link AsyncPlayerPreLoginEvent}
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleLogin(AsyncPlayerPreLoginEvent event)
    {
        if (event.getLoginResult() != Result.ALLOWED)
        {
            return;
        }

        LoginResult result = getLoginResult(event);

        if (result.isError())
        {
            event.disallow(Result.KICK_OTHER, result.getKickMessage());
            return;
        }

        NetworkUser user = result.isNewUser() ? createNewUser(event) : result.getUser();

        if (!checkWhitelist(user, event))
        {
            event.disallow(Result.KICK_OTHER, whitelistMessage);
            return;
        }

        event.allow();
        userManager.addUser(user);
    }

    /**
     * Determines the {@link LoginResult}
     * of an {@link AsyncPlayerPreLoginEvent}
     *
     * @param event The event
     *
     * @return The login result
     */
    private LoginResult getLoginResult(AsyncPlayerPreLoginEvent event)
    {
        if (!database.isConnected())
        {
            return new LoginResult(
                null, false, ChatColor.RED + "Database is unavailable, wait and try again"
            );
        }

        NetworkUser user = null;
        boolean newUser = false;

        String kickMessage = ChatColor.RED + "";

        try
        {
            user = database.fetchUser(event.getUniqueId())
                .get(loadTimeout.toMillis(), TimeUnit.MILLISECONDS);

            newUser = user == null;
        }
        catch (InterruptedException | ExecutionException ex)
        {
            kickMessage += "An error occured while loading your data";
            ErrorHandler.report(ex);
        }
        catch (TimeoutException ex)
        {
            kickMessage += "It took too long to load your data, try again";
        }

        return new LoginResult(user, newUser, kickMessage);
    }

    @Value
    private static class LoginResult
    {
        private final NetworkUser user;
        private final boolean newUser;
        private final String kickMessage;

        private boolean isError()
        {
            return user == null && !isNewUser();
        }
    }

    /**
     * Checks whether a {@link NetworkUser}
     * should be allowed to join the server,
     * and disallows the {@link AsyncPlayerPreLoginEvent}
     * if they are not
     *
     * @param user The user
     * @param event The login event
     *
     * @return Whether the user can join
     */
    private boolean checkWhitelist(NetworkUser user, AsyncPlayerPreLoginEvent event)
    {
        if (!user.hasRank(serverData.getRequiredRank()))
        {
            event.disallow(
                Result.KICK_WHITELIST, ChatColor.RED + "You don't have the required rank to join this server"
            );
            return false;
        }

        return true;
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
