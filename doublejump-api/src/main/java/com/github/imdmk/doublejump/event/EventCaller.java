package com.github.imdmk.doublejump.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a caller that can trigger Bukkit events.
 */
public interface EventCaller {

    /**
     * Calls (fires) the given Bukkit event.
     *
     * @param event the event to be called; must not be null
     */
    void callEvent(@NotNull Event event);
}
