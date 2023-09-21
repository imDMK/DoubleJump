package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.util.GameModeUtil;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class JumpPlayerService {

    private final RegionProvider regionProvider;
    private final JumpPlayerManager playerManager;

    private final List<String> disabledWorlds;
    private final List<GameMode> disabledGameModes;
    private final String doubleJumpUsePermission;

    private final boolean jumpsLimitEnabled;
    private final int jumpsLimit;
    private final Map<String, Integer> jumpsLimitByPermissions;

    public JumpPlayerService(RegionProvider regionProvider, JumpPlayerManager playerManager, List<String> disabledWorlds, List<GameMode> disabledGameModes, String doubleJumpUsePermission, boolean jumpsLimitEnabled, int jumpsLimit, Map<String, Integer> jumpsLimitByPermissions) {
        this.regionProvider = regionProvider;
        this.playerManager = playerManager;
        this.disabledWorlds = disabledWorlds;
        this.disabledGameModes = disabledGameModes;
        this.doubleJumpUsePermission = doubleJumpUsePermission;
        this.jumpsLimitEnabled = jumpsLimitEnabled;
        this.jumpsLimit = jumpsLimit;
        this.jumpsLimitByPermissions = jumpsLimitByPermissions;
    }

    /**
     * Denies flying, allows flight and adds a player to the map.
     *
     * @param player The player for whom to enable
     * @param force  Whether to skip checking if a player can use double jump
     * @return Whether the double jump mode has been enabled
     */
    public boolean enable(Player player, boolean force) {
        if (!force && !this.canUseDoubleJump(player)) {
            return false;
        }

        this.playerManager.add(player.getUniqueId(), this.create(player));

        player.setFlying(false);
        player.setAllowFlight(true);

        return true;
    }

    /**
     * Checks whether the player has the double jump mode enabled and whether he has permission to.
     * If so, it enables player to allow flight and disables flying.
     *
     * @return Whether changes have been made
     */
    public boolean refresh(Player player) {
        if (!this.playerManager.isDoubleJumpMode(player)) {
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
     * Removes a player from the map and disables flying only when a player has game mode that can't fly.
     *
     * @param player The player for whom to disable
     */
    public void disable(Player player) {
        UUID playerUniqueId = player.getUniqueId();
        GameMode playerGameMode = player.getGameMode();

        this.playerManager.remove(playerUniqueId);

        if (!GameModeUtil.isGameModeCanFly(playerGameMode)) {
            player.setAllowFlight(false);
        }
    }

    /**
     * Creates a jump player with a preset number of player jumps
     *
     * @param player For whom to create
     * @return The jump player that has been created
     */
    public JumpPlayer create(Player player) {
        AtomicReference<JumpPlayer> jumpPlayer = new AtomicReference<>(new JumpPlayer());

        if (this.jumpsLimitEnabled) {
            int availableJumps = this.getJumpsByPermission(player);

            jumpPlayer.set(new JumpPlayer(availableJumps, availableJumps));
        }

        this.playerManager.add(player.getUniqueId(), jumpPlayer.get());

        return jumpPlayer.get();
    }

    /**
     * Checks if the player is in a blocked world or has a blocked game mode. It then checks to see if the player has permission to double jump.
     *
     * @param player The player for whom to check
     * @return Whether the player can use double jump.
     */
    public boolean canUseDoubleJump(Player player) {
        if (this.regionProvider.isInRegion(player)) {
            return false;
        }

        GameMode playerGameMode = player.getGameMode();
        if (this.disabledGameModes.contains(playerGameMode)) {
            return false;
        }

        World playerWorld = player.getWorld();
        if (this.disabledWorlds.contains(playerWorld.getName())) {
            return false;
        }

        if (this.doubleJumpUsePermission == null || this.doubleJumpUsePermission.isEmpty()) {
            return true;
        }

        return player.hasPermission(this.doubleJumpUsePermission);
    }

    /**
     *
     * @param player The player
     * @return The {@link JumpPlayer}
     */
    public JumpPlayer getOrCreateJumpPlayer(Player player) {
        return this.playerManager.getJumpPlayer(player.getUniqueId())
                .orElseGet(() -> this.create(player));
    }

    /**
     * Checks how many maximum double jumps the player has.
     *
     * @param player The player for whom to check
     * @return The value of a player's maximum double jumps.
     */
    public int getJumpsByPermission(Player player) {
        for (Map.Entry<String, Integer> entry : this.jumpsLimitByPermissions.entrySet()) {
            String permission = entry.getKey();
            int jumpLimit = entry.getValue();

            if (player.hasPermission(permission)) {
                return jumpLimit;
            }
        }

        return this.jumpsLimit;
    }
}
