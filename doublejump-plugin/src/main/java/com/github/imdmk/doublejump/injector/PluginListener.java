package com.github.imdmk.doublejump.injector;

import org.bukkit.event.Listener;
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

}
