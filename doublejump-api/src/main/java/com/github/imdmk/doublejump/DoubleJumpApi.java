package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.config.ConfigManager;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Core public API interface for the DoubleJump plugin.
 * <p>
 * Provides access to the main components responsible for configuration management,
 * player state caching, restriction checking, and visual effect management related to double jumping.
 * This interface serves as the entry point for interacting with the plugin's core services.
 * </p>
 * <p>
 * Implementations of this interface are expected to be thread-safe and long-lived for the lifetime of the plugin.
 * </p>
 *
 * @since 1.0.0
 */
public interface DoubleJumpApi {

    /**
     * Retrieves the {@link ConfigManager} responsible for loading,
     * parsing, and providing access to all plugin configuration files.
     * <p>
     * This manager provides methods to obtain current configuration values
     * and supports dynamic reloads if implemented.
     * </p>
     *
     * @return a non-null instance of {@link ConfigManager} used by the plugin.
     */
    @NotNull
    ConfigManager getConfigurationManager();

    /**
     * Returns the {@link JumpPlayerCache} containing runtime data for all active players
     * utilizing the double jump feature.
     * <p>
     * This cache stores player-specific jump states and metadata, and is optimized
     * for concurrent access during gameplay.
     * </p>
     *
     * @return a non-null {@link JumpPlayerCache} instance managing player jump data.
     */
    @NotNull JumpPlayerCache getJumpPlayerCache();

    /**
     * Provides the {@link RestrictionChecker} used to evaluate if a player
     * is permitted to perform a double jump based on defined restrictions.
     * <p>
     * Restrictions may include world settings, player permissions, cooldowns,
     * or other custom rules.
     * </p>
     *
     * @return a non-null {@link RestrictionChecker} instance responsible for validation.
     */
    @NotNull RestrictionChecker getRestrictionChecker();

    /**
     * Retrieves the {@link JumpVisualCache} which caches visual effects related
     * to the double jump feature for improved performance and reduced resource usage.
     *
     * @return a non-null {@link JumpVisualCache} instance.
     */
    @NotNull JumpVisualCache getJumpVisualCache();

    /**
     * Provides access to the {@link JumpVisualRepository}, the persistent storage
     * layer for visual jump effects configuration and assets.
     * <p>
     * This repository handles loading, saving, and querying of visual effect data.
     * </p>
     *
     * @return a non-null {@link JumpVisualRepository} instance.
     */
    @NotNull JumpVisualRepository getJumpVisualRepository();
}
