package io.samdev.network.common.database.player;

import io.samdev.network.common.player.NetworkUser;
import io.samdev.network.common.player.Rank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkUserTest
{
    private static NetworkUser user;

    @BeforeAll
    public static void initUser()
    {
        user = mock(NetworkUser.class);
        when(user.getRank()).thenReturn(Rank.MOD);

        when(user.hasRank(any())).thenCallRealMethod();
        when(user.hasExactRank(any())).thenCallRealMethod();
    }

    @Test
    public void testHasRankTrueWhenRankOwned()
    {
        assertTrue(user.hasRank(Rank.MOD));
        assertTrue(user.hasRank(Rank.DEFAULT));

        assertFalse(user.hasRank(Rank.ADMIN));
    }

    @Test
    public void testHasRankExactTrueWhenSameRank()
    {
        assertTrue(user.hasExactRank(Rank.MOD));

        assertFalse(user.hasExactRank(Rank.HELPER));
        assertFalse(user.hasExactRank(Rank.ADMIN));
    }
}
