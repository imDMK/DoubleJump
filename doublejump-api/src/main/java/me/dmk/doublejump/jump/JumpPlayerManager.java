package me.dmk.doublejump.jump;

import me.dmk.doublejump.region.RegionProvider;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class used to change the player's double jump mode or check if the player has permission to use double jump.
*/
public class JumpPlayerManager {

    private final RegionProvider regionProvider;
    private final List<String> disabledWorlds;
    private final List<GameMode> disabledGameModes;
    private final String doubleJumpUsePermission;

    private final Map<UUID, JumpPlayer> jumpPlayers = new ConcurrentHashMap<>();

    public JumpPlayerManager(RegionProvider regionProvider, List<String> disabledWorlds, List<GameMode> disabledGameModes, String doubleJumpUsePermission) {
        this.regionProvider = regionProvider;
        this.disabledWorlds = disabledWorlds;
        this.disabledGameModes = disabledGameModes;
        this.doubleJumpUsePermission = doubleJumpUsePermission;
    }

    /**
     * Adds to the map.
     *
     * @param uuid The uuid to add
     * @param jumpPlayer The jump player to add
     * @return The jump player that has been added
     */
    public JumpPlayer add(UUID uuid, JumpPlayer jumpPlayer) {
        this.jumpPlayers.put(uuid, jumpPlayer);
        return jumpPlayer;
    }

    /**
     * Removes from the map.
     *
     * @param uuid The uuid to remove
     * @return The jump player associated with uuid or null
     */
    public JumpPlayer remove(UUID uuid) {
        return this.jumpPlayers.remove(uuid);
    }

    /**
     * Denies flying, allows flight and adds a player to the map.
     *
     * @param player The player for whom to enable
     * @param force Whether to skip checking if a player can use double jump
     * @return Whether the double jump mode has been enabled
     */
    public boolean enable(Player player, boolean force) {
        if (!force && !this.canUseDoubleJump(player)) {
            return false;
        }

        this.add(player.getUniqueId(), new JumpPlayer());

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
     * Removes a player from the map and disables flying only when a player has game mode that can't fly.
     *
     * @param player The player for whom to disable
     */
    public void disable(Player player) {
        UUID playerUniqueId = player.getUniqueId();
        GameMode playerGameMode = player.getGameMode();

        this.remove(playerUniqueId);

        if (!this.isGameModeCanFly(playerGameMode)) {
            player.setAllowFlight(false);
        }
    }

    /**
     * Checks if player has enabled double jump mode.
     *
     * @param player The player for whom to check
     * @return boolean whether the player has enabled double jump mode
     */
    public boolean isDoubleJumpMode(Player player) {
        return this.jumpPlayers.containsKey(player.getUniqueId());
    }

    /**
     * Checks if the player is in a blocked world or has a blocked game mode. It then checks to see if the player has permission to double jump.
     *
     * @param player The player for whom to check
     * @return Whether the player can use double jump.
     */
    public boolean canUseDoubleJump(Player player) {
        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();

        if (this.regionProvider.isInRegion(player)) {
            return false;
        }

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

    /**
     * Checks if the game mode is allowed to fly.
     *
     * @param gameMode Game mode to check
     * @return Whether the game mode has permission to fly
     */
    public boolean isGameModeCanFly(GameMode gameMode) {
        return gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE;
    }

    public List<String> getDisabledWorlds() {
        return this.disabledWorlds;
    }

    public List<GameMode> getDisabledGameModes() {
        return this.disabledGameModes;
    }

    public String getDoubleJumpUsePermission() {
        return this.doubleJumpUsePermission;
    }

    /**
     * @param uuid The uuid of player
     * @return Optional {@link JumpPlayer}
     */
    public Optional<JumpPlayer> getJumpPlayer(UUID uuid) {
        return Optional.ofNullable(this.jumpPlayers.get(uuid));
    }

    /**
     * A collection that has players who have double jump mode enabled.
     *
     * @return A map that contains the player's uuid as the key and the {@link JumpPlayer} as the value
     */
    public Map<UUID, JumpPlayer> getJumpPlayers() {
        return Collections.unmodifiableMap(this.jumpPlayers);
    }
}
