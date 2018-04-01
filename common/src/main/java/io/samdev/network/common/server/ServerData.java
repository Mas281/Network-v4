package io.samdev.network.common.server;

import io.samdev.network.common.player.Rank;
import lombok.Value;

/**
 * Data about an individual
 * Spigot server on the network
 */
@Value
public class ServerData
{
    /**
     * The server's name on the BungeeCord network
     */
    private final String serverName;

    /**
     * The {@link ReleaseType} of the server
     */
    private final ReleaseType releaseType;

    /**
     * The rank required to join the server
     */
    private final Rank requiredRank;
}
