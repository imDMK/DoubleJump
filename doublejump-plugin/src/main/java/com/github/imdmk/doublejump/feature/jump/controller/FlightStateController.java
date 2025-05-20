package com.github.imdmk.doublejump.feature.jump.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class FlightStateController extends PluginListener {

    @EventHandler(priority = EventPriority.LOW)
    void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        this.jumpCache.getActive(uuid)
                .ifPresent(jump -> {
                    jump.setActive(false);
                    this.flyingService.disable(player, true);
                });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpCache.isActive(player.getUniqueId())) {
            return;
        }

        if (!player.getAllowFlight()) {
            this.flyingService.enable(player);
        }
    }
}
