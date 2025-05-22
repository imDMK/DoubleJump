package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import org.jetbrains.annotations.NotNull;

/**
 * Public API interface for accessing core components of the DoubleJump plugin.
 * Provides access to configuration management and runtime player data.
 */
public interface DoubleJumpApi {

    /**
     * Returns the configuration manager responsible for loading and accessing
     * all plugin configuration files.
     *
     * @return the active {@link ConfigurationManager} instance
     */
    @NotNull ConfigurationManager getConfigurationManager();

    /**
     * Returns the player cache containing all active {@code JumpPlayer} instances.
     * Useful for accessing and modifying player-specific double jump data at runtime.
     *
     * @return the {@link JumpPlayerCache} instance
     */
    JumpPlayerCache getJumpPlayerCache();
}
