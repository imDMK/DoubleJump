package com.github.imdmk.doublejump.core.feature.jump.listener;

import com.github.imdmk.doublejump.core.feature.jump.JumpActivationType;
import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayer;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayerRepository;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.message.MessageService;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import com.github.imdmk.doublejump.core.shared.permission.PermissionBasedValueProvider;
import com.github.imdmk.doublejump.core.shared.permission.PlayerValueProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;

@PluginListener
final class JumpSessionListener implements Listener {

    private final MessageService messageService;
    private final FlightService flightService;
    private final JumpPlayerRepository jumpRepository;

    private final PlayerValueProvider<Boolean> enableOnJoinProvider;

    @Inject
    JumpSessionListener(
            JumpConfig config,
            MessageService messageService,
            FlightService flightService,
            JumpPlayerRepository jumpRepository
    ) {
        this.messageService = messageService;
        this.flightService = flightService;
        this.jumpRepository = jumpRepository;
        this.enableOnJoinProvider = new PermissionBasedValueProvider<>(config.enableOnJoin);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        boolean enableOnJoin = enableOnJoinProvider.resolve(player);
        if (enableOnJoin) {
            JumpPlayer jumpPlayer = JumpPlayer.create(playerId, JumpActivationType.JOIN);

            flightService.allowFlight(player);
            jumpRepository.activate(jumpPlayer);
            messageService.send(player, n -> n.jumpMessages.enabled());
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        JumpPlayer jumpPlayer = jumpRepository.deactivate(player.getUniqueId());
        if (jumpPlayer != null) {
            flightService.refreshFlightState(player);
        }
    }
}
