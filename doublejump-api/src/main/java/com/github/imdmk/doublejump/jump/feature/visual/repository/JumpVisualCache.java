package com.github.imdmk.doublejump.jump.feature.visual.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Cache layer for storing and retrieving {@link JumpVisual} settings based on player UUIDs.
 * <p>
 * Uses Caffeine in-memory cache with configurable expiration policies.
 * Provides fast access to frequently used visual configuration data.
 */
public class JumpVisualCache {

    /**
     * Default expiration duration after write (30 minutes).
     */
    private static final Duration EXPIRE_AFTER_WRITE = Duration.ofMinutes(30);

    /**
     * Default expiration duration after access (30 minutes).
     */
    private static final Duration EXPIRE_AFTER_ACCESS = Duration.ofMinutes(30);

    private final Cache<UUID, JumpVisual> cacheByUuid;

    /**
     * Creates a new {@link JumpVisualCache} with custom expiration durations.
     *
     * @param expireAfterWrite  duration after which cache entries expire after being written
     * @param expireAfterAccess duration after which cache entries expire after last access
     */
    public JumpVisualCache(@NotNull Duration expireAfterWrite, @NotNull Duration expireAfterAccess) {
        this.cacheByUuid = Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWrite)
                .expireAfterAccess(expireAfterAccess)
                .maximumSize(1000)
                .build();
    }

    /**
     * Creates a new {@link JumpVisualCache} with default expiration durations.
     */
    public JumpVisualCache() {
        this(EXPIRE_AFTER_WRITE, EXPIRE_AFTER_ACCESS);
    }

    /**
     * Caches the given visual settings for the specified player UUID.
     *
     * @param uuid     unique player identifier
     * @param settings visual settings to cache
     */
    public void cache(@NotNull UUID uuid, @NotNull JumpVisual settings) {
        this.cacheByUuid.put(uuid, settings);
    }

    /**
     * Evicts the cached visual settings for the specified player UUID.
     *
     * @param uuid unique player identifier whose settings should be removed
     */
    public void evict(@NotNull UUID uuid) {
        this.cacheByUuid.invalidate(uuid);
    }

    /**
     * Clears all cached visual settings.
     */
    public void clearCache() {
        this.cacheByUuid.invalidateAll();
    }

    /**
     * Retrieves cached visual settings for the given player UUID.
     *
     * @param uuid unique player identifier
     * @return optional visual settings, or empty if not cached
     */
    public Optional<JumpVisual> getByUuid(@NotNull UUID uuid) {
        return Optional.ofNullable(this.cacheByUuid.getIfPresent(uuid));
    }

    /**
     * Returns an unmodifiable collection of all currently cached {@link JumpVisual} entries.
     *
     * @return collection of cached visual settings
     */
    public @NotNull Collection<JumpVisual> getAllCachedVisuals() {
        return Collections.unmodifiableCollection(this.cacheByUuid.asMap().values());
    }
}
