package com.github.imdmk.doublejump.core.platform.flight;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.bukkit.GameMode;

@Service(priority = ComponentPriority.LOWEST)
final class FlightPolicy {

    boolean canFly(GameMode mode) {
        return mode == GameMode.CREATIVE
                || mode == GameMode.SPECTATOR;
    }
}
