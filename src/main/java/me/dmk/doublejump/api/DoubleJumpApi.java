package me.dmk.doublejump.api;

import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.entity.Player;

public interface DoubleJumpApi {

    /**
     * Denies flying, allows flight and adds a player to {@link JumpPlayerMap}
     * @param player The player for whom to enable
     * @return Whether the double jump mode has been enabled
     */
    boolean enable(Player player);

    /**
     * Checks if the player is in a blocked world or has a blocked game mode. It then checks to see if the player has permission to double jump.
     * @param player The player for whom to check
     * @return Whether the player can use double jump.
     */
    boolean canUseDoubleJump(Player player);

    /**
     * Checks if player has enabled double jump mode
     * @param player The player for whom to check
     * @return Whether the player has enabled double jump mode
     */
    boolean isDoubleJumpMode(Player player);

    /**
     * Disables flying only when player has game mode that can't fly and removes player from the {@link JumpPlayerMap}
     * @param player The player for whom to disable
     */
    void disable(Player player);
}
