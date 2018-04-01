package io.samdev.network.common.player;

import io.samdev.network.common.database.mongo.DatabaseObject;
import lombok.Getter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.Instant;
import java.util.UUID;

/**
 * Class holding a network user's permanent data
 */
@Entity(value = "users", noClassnameStored = true)
@Getter
public class NetworkUser extends DatabaseObject
{
    @Id
    private final UUID id;

    private Rank rank;
    private String prefix;

    private Instant firstJoin;

    private int coins;

    public NetworkUser(UUID uuid)
    {
        this.id = uuid;

        this.rank = Rank.DEFAULT;
        this.prefix = rank.getRankName();

        this.firstJoin = Instant.now();

        this.coins = 0;
    }
}
