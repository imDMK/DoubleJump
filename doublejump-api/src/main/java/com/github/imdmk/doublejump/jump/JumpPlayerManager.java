package com.github.imdmk.doublejump.jump;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class used to change the player's double jump mode or check if the player has permission to use double jump.
*/
public class JumpPlayerManager {

    private final Map<UUID, JumpPlayer> jumpPlayers = new ConcurrentHashMap<>();

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
     * Checks if player has enabled double jump mode.
     *
     * @param player The player for whom to check
     * @return boolean whether the player has enabled double jump mode
     */
    public boolean isDoubleJumpMode(Player player) {
        return this.jumpPlayers.containsKey(player.getUniqueId());
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
