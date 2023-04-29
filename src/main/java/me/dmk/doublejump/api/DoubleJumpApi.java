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
     * Checks if player has permission to use double jump
     * @param player The player for whom to check
     * @return Whether the player have permission to use double jump
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
