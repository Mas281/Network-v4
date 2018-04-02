package io.samdev.network.common.player;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * The player ranks on the network
 */
@Getter
public enum Rank
{
    OWNER("Owner", ChatColor.DARK_RED, ChatColor.RED),
    ADMIN("Admin", ChatColor.RED, ChatColor.RED),
    DEV("Developer", ChatColor.YELLOW, ChatColor.YELLOW),

    MOD("Mod", ChatColor.DARK_GREEN, ChatColor.GREEN),
    HELPER("Helper", ChatColor.DARK_AQUA, ChatColor.AQUA),

    MVP("MVP", ChatColor.AQUA, ChatColor.AQUA),
    VIP("VIP", ChatColor.GREEN, ChatColor.GREEN),

    DEFAULT("", null, ChatColor.GRAY);

    Rank(String rankName, ChatColor tagColour, ChatColor nameColour)
    {
        this.rankName = rankName;

        this.tagColour = tagColour;
        this.nameColour = nameColour;
    }

    /**
     * The display name of the rank
     */
    private final String rankName;

    /**
     * The tag colour of the rank
     */
    private final ChatColor tagColour;

    /**
     * The name colour for the rank
     */
    private final ChatColor nameColour;
}
