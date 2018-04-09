package io.samdev.network.server.core.player;

import io.samdev.network.common.player.NetworkUser;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Class handling the storage
 * of {@link OnlineUser} objects
 */
public class UserManager
{
    /**
     * Map of online player {@link UUID}
     * objects to their {@link OnlineUser} objects
     */
    private final Map<UUID, OnlineUser> onlineUsers = new WeakHashMap<>();

    /**
     * Fetches the {@link OnlineUser}
     * object linked to a {@link Player}
     *
     * @see #onlineUsers
     *
     * @param player The player
     *
     * @return The player's {@link OnlineUser} object
     */
    public OnlineUser getUser(Player player)
    {
        return onlineUsers.get(player.getUniqueId());
    }

    /**
     * Adds a user to the user map
     *
     * @see #onlineUsers
     *
     * @param user The user
     */
    public void addUser(NetworkUser user)
    {
        onlineUsers.put(user.getId(), new OnlineUser(user));
    }

    /**
     * Handles a player joining the server
     *
     * @see OnlineUser#setPlayer(Player, boolean)
     *
     * @param player The player
     */
    public void handleJoin(Player player)
    {
        // todo: determine proxy join

        getUser(player).setPlayer(player, false);
    }
}