package com.github.imdmk.doublejump.feature.jump.restriction.delay;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.Instant;

public class DelayRestrictionController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(final DoubleJumpEvent event) {
        event.getJumpPlayer().setLastJump(Instant.now());
    }
}
