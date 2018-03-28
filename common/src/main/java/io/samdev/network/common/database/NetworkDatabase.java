package io.samdev.network.common.database;

/**
 * Abstraction of a database service for the network
 */
public interface NetworkDatabase
{
    /**
     * Attempts to connect to the database
     */
    void connect();

    /**
     * Disconnects from the database
     */
    void disconnect();

    /**
     * Determines whether a database connection is active
     *
     * @return Whether a connection has been established
     */
    boolean isConnected();
}
