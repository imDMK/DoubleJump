package com.github.imdmk.doublejump.core.feature.jump;

import com.github.imdmk.doublejump.core.feature.jump.cooldown.JumpCooldownService;
import com.github.imdmk.doublejump.core.feature.jump.message.JumpMessageService;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.injector.annotations.Task;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;

@Task(delayMillis = 0, periodMillis = 100, async = false)
final class JumpFlightUpdateTask implements Runnable {

    private final Server server;
    private final JumpConfig config;
    private final FlightService flightService;
    private final JumpCooldownService cooldownService;
    private final JumpPlayerRepository jumpRepository;
    private final JumpMessageService jumpMessageService;

    @Inject
    JumpFlightUpdateTask(
            Server server,
            JumpConfig config,
            FlightService flightService,
            JumpCooldownService cooldownService,
            JumpPlayerRepository jumpRepository,
            JumpMessageService jumpMessageService
    ) {
        this.server = server;
        this.config = config;
        this.flightService = flightService;
        this.cooldownService = cooldownService;
        this.jumpRepository = jumpRepository;
        this.jumpMessageService = jumpMessageService;
    }

    @Override
    public void run() {
        for (JumpPlayer jumpPlayer : jumpRepository.getActivePlayers()) {
            UUID playerId = jumpPlayer.getUuid();

            Player player = server.getPlayer(playerId);
            if (player == null) {
                jumpRepository.deactivate(playerId);
                continue;
            }

            if (cooldownService.isOnCooldown(playerId)) {
                jumpMessageService.notify(player, JumpResult.COOLDOWN);
                continue;
            }

            if (shouldRestoreFlight(player)) {
                jumpMessageService.notify(player, JumpResult.ALLOWED);
                flightService.allowFlight(player);
            }
        }
    }

    private boolean shouldRestoreFlight(Player player) {
        if (config.enableFallDamage
                && player.getFallDistance() >= 4.0f
                && player.getVelocity().getY() < 0) {
            return false;
        }

        return !player.getAllowFlight();
    }
}
