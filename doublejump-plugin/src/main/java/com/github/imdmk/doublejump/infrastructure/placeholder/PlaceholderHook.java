package com.github.imdmk.doublejump.infrastructure.placeholder;

import com.github.imdmk.doublejump.jump.feature.placeholder.JumpPlaceholderConfig;
import org.bukkit.Server;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Hook responsible for detecting and managing PlaceholderAPI integration.
 */
public final class PlaceholderHook {

    private static final String PLACEHOLDER_API_PLUGIN = "PlaceholderAPI";

    private final Server server;
    private final JumpPlaceholderConfig config;

    public PlaceholderHook(@NotNull Server server, @NotNull JumpPlaceholderConfig config) {
        this.server = Objects.requireNonNull(server, "server cannot be null");
        this.config = Objects.requireNonNull(config, "config cannot be null");
    }

    /**
     * Returns true if integration is enabled in configuration.
     *
     * @return true if integration should be active
     */
    public boolean isIntegrationEnabled() {
        return this.config.enabled;
    }

    /**
     * Returns true if the PlaceholderAPI plugin is installed and enabled.
     *
     * @return true if PlaceholderAPI plugin is present
     */
    public boolean isPluginPresent() {
        return this.server.getPluginManager().isPluginEnabled(PLACEHOLDER_API_PLUGIN);
    }

    /**
     * Returns true if integration is enabled and PlaceholderAPI is present.
     *
     * @return true if integration is available
     */
    public boolean isAvailable() {
        return this.isIntegrationEnabled() && this.isPluginPresent();
    }

    /**
     * Asserts PlaceholderAPI is available. Throws if not.
     *
     * @throws IllegalStateException if integration is disabled or PlaceholderAPI is missing
     */
    @ApiStatus.Internal
    public void assertAvailable() {
        if (!this.isAvailable()) {
            throw new IllegalStateException("PlaceholderAPI is not available or integration is disabled.");
        }
    }
}
