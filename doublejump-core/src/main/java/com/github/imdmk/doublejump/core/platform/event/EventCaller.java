package com.github.imdmk.doublejump.core.platform.event;

import org.bukkit.event.Event;

public interface EventCaller {

    <E extends Event> E callEvent(E event);

}
