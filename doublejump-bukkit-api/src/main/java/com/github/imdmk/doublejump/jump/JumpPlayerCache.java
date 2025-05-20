package com.github.imdmk.doublejump.jump;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe cache storing {@link JumpPlayer} instances by their UUIDs.
 * Used to track players with active double jump capabilities.
 * <p>
 * The cache supports typical operations: add, remove, check existence,
 * retrieve, and list all registered players.
 * </p>
 */
public class JumpPlayerCache {

    private final Map<UUID, JumpPlayer> jumpPlayers = new ConcurrentHashMap<>();

    /**
     * Adds or replaces a {@link JumpPlayer} in the cache.
     *
     * @param uuid       The UUID of the player.
     * @param jumpPlayer The {@link JumpPlayer} instance to store.
     */
    public void put(UUID uuid, JumpPlayer jumpPlayer) {
        this.jumpPlayers.put(uuid, jumpPlayer);
    }

    /**
     * Removes the {@link JumpPlayer} associated with the given UUID.
     *
     * @param uuid The UUID of the player.
     * @return {@code true} if a player was removed, {@code false} if none was found.
     */
    public boolean remove(UUID uuid) {
        return this.jumpPlayers.remove(uuid) != null;
    }

    /**
     * Checks whether the cache contains a {@link JumpPlayer} for the given UUID.
     *
     * @param uuid The UUID to check.
     * @return {@code true} if a player is present, {@code false} otherwise.
     */
    public boolean hasPlayer(UUID uuid) {
        return this.jumpPlayers.containsKey(uuid);
    }

    /**
     * Checks whether the {@link JumpPlayer} associated with the given UUID is active.
     *
     * @param uuid The UUID of the player.
     * @return {@code true} if the player is present and active, {@code false} otherwise.
     */
    public boolean isActive(UUID uuid) {
        return this.getActive(uuid).isPresent();
    }

    /**
     * Retrieves the active {@link JumpPlayer} for the given UUID, if present.
     * The player must be marked as active.
     *
     * @param uuid The UUID of the player.
     * @return An {@link Optional} containing the active {@link JumpPlayer} if present, or empty otherwise.
     */
    public Optional<JumpPlayer> getActive(UUID uuid) {
        return this.get(uuid).filter(JumpPlayer::isActive);
    }

    /**
     * Retrieves the {@link JumpPlayer} for the given UUID, if present.
     *
     * @param uuid The UUID of the player.
     * @return An {@link Optional} containing the {@link JumpPlayer} if present, or empty otherwise.
     */
    public Optional<JumpPlayer> get(UUID uuid) {
        return Optional.ofNullable(this.jumpPlayers.get(uuid));
    }

    /**
     * Returns an unmodifiable view of all stored {@link JumpPlayer}s.
     *
     * @return An unmodifiable map containing all cached players by their UUID.
     */
    public Map<UUID, JumpPlayer> getPlayers() {
        return Collections.unmodifiableMap(this.jumpPlayers);
    }
}
