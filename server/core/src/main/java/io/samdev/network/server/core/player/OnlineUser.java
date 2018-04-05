package io.samdev.network.server.core.player;

import io.samdev.network.common.player.NetworkUser;
import io.samdev.network.common.player.Rank;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.entity.Player;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The representation of a {@link NetworkUser}
 * whom is currently online on the server
 */
@RequiredArgsConstructor
public class OnlineUser
{
    /**
     * The online user's {@link NetworkUser} object
     *
     * The {@link Delegate} annotation from Lombok
     * is used to allow any methods from the
     * {@link NetworkUser} class to be used
     * directly from {@link OnlineUser} instances
     */
    @Delegate
    private final NetworkUser user;

    /**
     * The user's Bukkit {@link Player} object
     */
    private Player player;

    /**
     * Sets the user's {@link Player} object
     *
     * @param player The player object
     * @param proxyJoin Whether the player has just joined through the BungeeCord proxy
     */
    public void setPlayer(Player player, boolean proxyJoin)
    {
        checkArgument(this.player == null, "Player is already set");

        this.player = player;

        // Update latest join if
        // joining through BungeeCord
        if (proxyJoin && !isNewUser())
        {
            updateLastJoin(Instant.now());
        }

        // Check for name changes
        if (!getName().equals(player.getName()))
        {
            updateName(player.getName());
        }

        // Grant developers and above operator permissions
        player.setOp(hasRank(Rank.DEV));
    }
}
