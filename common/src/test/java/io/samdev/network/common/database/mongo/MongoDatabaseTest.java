package io.samdev.network.common.database.mongo;

import io.samdev.network.common.database.NetworkDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoDatabaseTest
{
    private static NetworkDatabase database;

    @BeforeAll
    public static void connectToDatabase()
    {
        database = new MongoDatabase();
        database.connect();
    }

    @Test
    public void testConnectionStatusTrueWhenConnected()
    {
        assertTrue(database.isConnected(), "Database is not connected");
    }

    @AfterAll
    public static void disconnectFromDatabase()
    {
        database.disconnect();
    }
}
