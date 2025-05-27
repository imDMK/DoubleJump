package com.github.imdmk.doublejump.infrastructure.injector;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

/**
 * Base class for Bukkit event listeners that are automatically registered
 * with the plugin's event manager after dependency injection.
 * <p>
 * Extends {@link DefaultInjectable} to provide injected dependencies.
 * Implements {@link Listener} to handle Bukkit events.
 * </p>
 */
public abstract class PluginListener extends DefaultInjectable implements Listener {

    /**
     * Automatically registers this listener instance with the Bukkit plugin manager.
     */
    @PostConstruct
    public void postConstruct() {
        this.server.getPluginManager().registerEvents(this, this.plugin);
    }

    /**
     * Checks whether two locations refer to the same block position (ignoring exact coordinates).
     * <p>
     * This method compares the block coordinates (X, Y, Z) of the given locations.
     * If the second location is {@code null}, the method returns {@code true} by design,
     * assuming the positions are considered the same in that context.
     * </p>
     *
     * @param from the first location to compare; must not be {@code null}
     * @param to   the second location to compare; may be {@code null}
     * @return {@code true} if {@code to} is {@code null} or both locations are in the same block; {@code false} otherwise
     */
    protected boolean isSameBlockPosition(@NotNull final Location from, @Nullable final Location to) {
        if (to == null) {
            return true;
        }

        return from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ();
    }

}
