package io.samdev.network.common.database;

import io.samdev.network.common.player.NetworkUser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Abstraction of the main database service for the network
 */
public interface NetworkDatabase extends Connectable
{
    /**
     * Fetches a {@link NetworkUser}
     * from the database in the form
     * of a {@link CompletableFuture}
     *
     * @param id The user's ID
     *
     * @return CompletableFuture holding the user
     */
    CompletableFuture<NetworkUser> fetchUser(UUID id);
}
