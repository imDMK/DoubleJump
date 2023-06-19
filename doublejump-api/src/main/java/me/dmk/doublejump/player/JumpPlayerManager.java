package me.dmk.doublejump.player;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
* Class used to change the player's double jump mode with change of flying and flying ability.
* And you can use it to check if the player has permission to double jump.
*/
public class JumpPlayerManager {

    private final List<String> disabledWorlds;
    private final List<GameMode> disabledGameModes;
    private final String doubleJumpUsePermission;

    private final JumpPlayerMap jumpPlayerMap;

    public JumpPlayerManager(List<String> disabledWorlds, List<GameMode> disabledGameModes, String doubleJumpUsePermission, JumpPlayerMap jumpPlayerMap) {
        this.disabledWorlds = disabledWorlds;
        this.disabledGameModes = disabledGameModes;
        this.doubleJumpUsePermission = doubleJumpUsePermission;
        this.jumpPlayerMap = jumpPlayerMap;
    }

    /**
     * Denies flying, allows flight and adds a player to {@link JumpPlayerMap}
     * @param player The player for whom to enable
     * @param force Whether to skip checking if a player can use double jump.
     * @return Whether the double jump mode has been enabled
     */
    public boolean enable(Player player, boolean force) {
        if (!force && !this.canUseDoubleJump(player)) {
            return false;
        }

        this.jumpPlayerMap.add(player);

        player.setFlying(false);
        player.setAllowFlight(true);
        return true;
    }

    /**
     * Checks whether the player has the double jump mode enabled and whether he has permission to.
     * If it has, it changes flying to false and ability to fly to true.
     *
     * @return Whether changes have been made
     */
    public boolean refresh(Player player) {
        if (!this.isDoubleJumpMode(player)) {
            return false;
        }

        if (!this.canUseDoubleJump(player)) {
            return false;
        }

        player.setFlying(false);
        player.setAllowFlight(true);
        return true;
    }

    /**
     * Disables flying only when player has game mode that can't fly and removes player from the {@link JumpPlayerMap}
     * @param player The player for whom to disable
     */
    public void disable(Player player) {
        this.jumpPlayerMap.remove(player);

        GameMode playerGameMode = player.getGameMode();
        if (playerGameMode == GameMode.SURVIVAL || playerGameMode == GameMode.ADVENTURE) {
            player.setAllowFlight(false);
        }
    }

    /**
     * Checks if player has enabled double jump mode
     * @param player The player for whom to check
     * @return boolean whether the player has enabled double jump mode
     */
    public boolean isDoubleJumpMode(Player player) {
        return this.jumpPlayerMap.contains(player);
    }

    /**
     * Checks if the player is in a blocked world or has a blocked game mode. It then checks to see if the player has permission to double jump.
     * @param player The player for whom to check
     * @return Whether the player can use double jump.
     */
    public boolean canUseDoubleJump(Player player) {
        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();

        if (this.disabledGameModes.contains(playerGameMode)) {
            return false;
        }

        if (this.disabledWorlds.contains(playerWorld.getName())) {
            return false;
        }

        if (this.doubleJumpUsePermission == null || this.doubleJumpUsePermission.isEmpty()) {
            return true;
        }

        return player.hasPermission(this.doubleJumpUsePermission);
    }

    public String getDoubleJumpUsePermission() {
        return this.doubleJumpUsePermission;
    }
}
