package com.github.imdmk.doublejump.core.feature.jump.listener;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpExecutor;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayer;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayerRepository;
import com.github.imdmk.doublejump.core.feature.jump.cooldown.JumpCooldownService;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;

@PluginListener
final class JumpPerformListener implements Listener {

    private final JumpConfig config;
    private final FlightService flightService;
    private final JumpCooldownService cooldownService;
    private final JumpPlayerRepository jumpRepository;
    private final JumpExecutor jumpExecutor;

    @Inject
    JumpPerformListener(
            JumpConfig config,
            FlightService flightService,
            JumpCooldownService cooldownService,
            JumpPlayerRepository jumpRepository,
            JumpExecutor jumpExecutor
    ) {
        this.config = config;
        this.flightService = flightService;
        this.cooldownService = cooldownService;
        this.jumpRepository = jumpRepository;
        this.jumpExecutor = jumpExecutor;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        JumpPlayer jump = jumpRepository.get(playerId);
        if (jump == null) {
            return;
        }

        event.setCancelled(true);

        JumpResult result = jumpExecutor.execute(player, jump);
        if (result.isDisableJumpMode()) {
            disable(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        JumpPlayer jumpPlayer = jumpRepository.get(playerId);
        if (jumpPlayer == null) {
            return;
        }

        cooldownService.reset(playerId);

        if (config.disableAfterDeath) {
            disable(player);
            return;
        }

        flightService.allowFlight(player);
    }

    private void disable(Player player) {
        flightService.refreshFlightState(player);
        jumpRepository.deactivate(player.getUniqueId());
    }
}
