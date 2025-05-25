package com.github.imdmk.doublejump.feature.jump.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class FlightStateController extends PluginListener {

    @EventHandler(priority = EventPriority.LOW)
    void onPlayerQuit(final PlayerQuitEvent event) {
        this.jumpCache.ifActive(event.getPlayer().getUniqueId(), jump -> this.flyingService.disable(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW)
    void onServerReload(final ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) {
            return;
        }

        this.server.getOnlinePlayers().forEach(this.flyingService::disable);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || to.getBlockX() == from.getBlockX()
                && to.getBlockY() == from.getBlockY()
                && to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        this.jumpCache.ifActive(player.getUniqueId(), jump -> {
            if (jump.isJumpAllowed() && !player.getAllowFlight()) {
                this.flyingService.enable(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();

        this.jumpCache.getActive(player.getUniqueId())
                .filter(jump -> jump.isActivationType(JumpActivationType.JOIN) || jump.isActivationType(JumpActivationType.COMMAND))
                .ifPresent(jump -> {
                    jump.setJumpAllowed(true);
                    jump.setLastNotifiedReason(null);
                    jump.setLastJump(null);

                    this.flyingService.enable(player);
                });
    }
}
