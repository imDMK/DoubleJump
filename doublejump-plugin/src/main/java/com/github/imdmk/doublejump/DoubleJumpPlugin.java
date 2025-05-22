package com.github.imdmk.doublejump;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main Bukkit plugin entry point for the DoubleJump plugin.
 * <p>
 * Responsible for initializing and shutting down the plugin lifecycle
 * via the {@link JavaPlugin} lifecycle methods {@code onEnable()} and {@code onDisable()}.
 */
public class DoubleJumpPlugin extends JavaPlugin {

    /** bStats Metrics service ID for reporting plugin statistics */
    public static final int METRICS_SERVICE_ID = 19387;

    /**
     * Called by Bukkit when the plugin is being enabled.
     * <p>
     * Initializes the main {@link DoubleJump} instance which sets up
     * configurations, services, commands, and listeners.
     */
    private DoubleJump doubleJump;

    /**
     * The main Bukkit plugin entry point for the DoubleJump plugin.
     * <p>
     * Responsible for initializing and shutting down the plugin lifecycle
     * via the {@link JavaPlugin} lifecycle methods {@code onEnable()} and {@code onDisable()}.
     */
    @Override
    public void onEnable() {
        this.doubleJump = new DoubleJump(this);
    }

    /**
     * Called by Bukkit when the plugin is being disabled.
     * <p>
     * Cleans up plugin resources and shuts down active services safely.
     */
    @Override
    public void onDisable() {
        this.doubleJump.disable();
        this.doubleJump = null;
    }
}
