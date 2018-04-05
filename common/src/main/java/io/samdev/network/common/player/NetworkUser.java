package io.samdev.network.common.player;

import io.samdev.network.common.database.mongo.DatabaseObject;
import lombok.Getter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

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

    @Indexed(options = @IndexOptions(unique = true))
    private String name;

    private Rank rank;
    private String prefix;

    private Instant firstJoin;
    private Instant lastJoin;

    private int coins;

    private transient boolean newUser;

    public NetworkUser(UUID uuid, String name)
    {
        this.id = uuid;
        this.name = name;

        this.rank = Rank.DEFAULT;
        this.prefix = rank.getRankName();

        this.firstJoin = Instant.now();
        this.lastJoin = firstJoin;

        this.coins = 0;

        this.newUser = true;
    }

    /**
     * Updates the user's stored name
     * after a detected name change
     *
     * @see #name
     *
     * @param newName The user's new name
     */
    public void updateName(String newName)
    {
        this.name = newName;

        // TODO: update in database
    }

    /**
     * Updates a user's last
     * joined instant
     *
     * @see #lastJoin
     *
     * @param instant The instant the user joined
     */
    public void updateLastJoin(Instant instant)
    {
        this.lastJoin = instant;

        // TODO: update in database
    }

    /**
     * Determines whether the user has
     * an equal or higher rank than the
     * one specified
     *
     * @param rank The rank to check
     *
     * @return Whether the user has the rank
     */
    public boolean hasRank(Rank rank)
    {
        return getRank().compareTo(rank) <= 0;
    }

    /**
     * Determines whether the user's rank
     * is exactly any of the ones specified
     *
     * @param ranks The ranks to check
     *
     * @return Whether the user's rank matches any of the specified ranks
     */
    public boolean hasExactRank(Rank... ranks)
    {
        for (Rank rank : ranks)
        {
            if (getRank() == rank)
            {
                return true;
            }
        }

        return false;
    }
}
