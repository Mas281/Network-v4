package io.samdev.network.common.database;

/**
 * The representation of a resource that
 * can be connected to, for example a MongoDB
 * database or a Redis server
 */
public interface Connectable
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
