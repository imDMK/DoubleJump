package com.github.imdmk.doublejump.injector;

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

    protected boolean isSameBlockPosition(@NotNull final Location from, @Nullable final Location to) {
        if (to == null) {
            return true;
        }

        return from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ();
    }

}
