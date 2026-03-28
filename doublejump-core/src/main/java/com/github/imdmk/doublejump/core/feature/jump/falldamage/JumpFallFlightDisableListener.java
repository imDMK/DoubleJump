package com.github.imdmk.doublejump.core.feature.jump.falldamage;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayer;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayerRepository;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

@PluginListener
final class JumpFallFlightDisableListener implements Listener {

    private final JumpConfig config;
    private final JumpPlayerRepository jumpRepository;
    private final FlightService flightService;

    @Inject
    JumpFallFlightDisableListener(
            JumpConfig config,
            JumpPlayerRepository jumpRepository,
            FlightService flightService
    ) {
        this.config = config;
        this.jumpRepository = jumpRepository;
        this.flightService = flightService;
    }

    @EventHandler(ignoreCancelled = true)
    void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo() != null &&
                event.getFrom().getX() == event.getTo().getX() &&
                event.getFrom().getY() == event.getTo().getY() &&
                event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }

        if (!config.enableFallDamage) {
            return;
        }

        JumpPlayer jumpPlayer = jumpRepository.get(player.getUniqueId());
        if (jumpPlayer == null) {
            return;
        }

        if (player.isFlying() || player.isGliding()) {
            return;
        }

        if (player.getFallDistance() >= 4.0f) {
            if (!player.getAllowFlight()) {
                return;
            }

            flightService.disallowFlight(player);
        }
    }
}
