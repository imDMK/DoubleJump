package com.github.imdmk.doublejump.core.feature.jump.item.listener;

import com.github.imdmk.doublejump.core.feature.jump.JumpActivationType;
import com.github.imdmk.doublejump.core.feature.jump.JumpExecutor;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayer;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayerRepository;
import com.github.imdmk.doublejump.core.feature.jump.cooldown.JumpCooldownService;
import com.github.imdmk.doublejump.core.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.core.feature.jump.item.config.JumpItemConfig;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.message.MessageService;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;

@PluginListener
final class JumpItemListener implements Listener {

    private final JumpItemConfig config;
    private final FlightService flightService;
    private final JumpCooldownService cooldownService;
    private final JumpPlayerRepository jumpRepository;
    private final MessageService messageService;
    private final JumpExecutor jumpExecutor;
    private final JumpItemService itemService;

    @Inject
    JumpItemListener(
            JumpItemConfig config,
            FlightService flightService,
            JumpCooldownService cooldownService,
            JumpPlayerRepository jumpRepository,
            MessageService messageService,
            JumpExecutor jumpExecutor,
            JumpItemService itemService
    ) {
        this.config = config;
        this.flightService = flightService;
        this.cooldownService = cooldownService;
        this.messageService = messageService;
        this.jumpExecutor = jumpExecutor;
        this.jumpRepository = jumpRepository;
        this.itemService = itemService;
    }

    @EventHandler(ignoreCancelled = true)
    void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!itemService.isEnabled() && !itemService.isClickMode()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpRepository.get(playerId);
        boolean isActive = jumpPlayer != null;
        boolean isUsing = itemService.isUsingItem(player);

        if (isActive && jumpPlayer.getActivationType() != JumpActivationType.ITEM) {
            return;
        }

        boolean isCooldown = cooldownService.isOnCooldown(playerId);
        if (!isActive && isUsing && !isCooldown) {
            JumpPlayer created = JumpPlayer.create(playerId, JumpActivationType.ITEM);

            flightService.allowFlight(player);
            jumpRepository.activate(created);

            messageService.send(player, n -> n.jumpMessages.enabled());
            return;
        }

        if (isActive && !isUsing) {
            flightService.refreshFlightState(player);
            jumpRepository.deactivate(playerId);

            messageService.send(player, n -> n.jumpMessages.disabled());
        }
    }

    @EventHandler(ignoreCancelled = true)
    void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!itemService.isEnabled() || !itemService.isClickMode()) {
            return;
        }

        if (!itemService.isJumpItem(event.getItem())) {
            return;
        }

        JumpPlayer jumpPlayer = JumpPlayer.create(playerId, JumpActivationType.ITEM);
        jumpExecutor.execute(player, jumpPlayer);
    }
}
