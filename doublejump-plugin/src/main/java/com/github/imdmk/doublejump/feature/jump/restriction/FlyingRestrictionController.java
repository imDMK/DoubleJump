package com.github.imdmk.doublejump.feature.jump.restriction;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles world join and change events to conditionally enable double jump for players
 * based on their permissions, operator status, and configuration rules.
 */
public class FlyingRestrictionController extends PluginListener {

    @EventHandler(priority = EventPriority.LOWEST)
    void onDoubleJump(final DoubleJumpEvent event) {
        Player player = event.getPlayer();

        this.jumpCache.getActive(player.getUniqueId())
                .filter(jump -> this.restrictionService.isRestricted(player))
                .ifPresent(jump -> {
                    event.setCancelled(true); // cancel event

                    // disable double jump
                    jump.setActive(false);
                    this.flyingService.disable(player);
                });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        this.jumpCache.getActive(player.getUniqueId())
                .filter(jump -> this.restrictionService.isRestricted(player))
                .ifPresent(jump -> {
                    jump.setActive(false);
                    this.flyingService.disable(player);
                });
    }


    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerJoin(final PlayerJoinEvent event) {
        this.attemptEnableDoubleJump(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        this.attemptEnableDoubleJump(event.getPlayer());
    }

    private void attemptEnableDoubleJump(@NotNull Player player) {
        this.jumpCache.get(player.getUniqueId())
                .filter(jump -> this.shouldEnable(player))
                .ifPresent(jump -> {
                    jump.setActive(true);
                    this.flyingService.enable(player);
                });
    }

    private boolean shouldEnable(@NotNull Player player) {
        if (this.restrictionService.isRestricted(player)) {
            return false;
        }

        if (player.isOp() && !this.jumpConfiguration.autoEnableForAdmins) {
            return false;
        }

        boolean hasPermission = player.hasPermission(this.jumpConfiguration.autoEnablePermission);
        boolean globallyEnabled = this.jumpConfiguration.autoEnableOnJoin;

        return hasPermission || globallyEnabled;
    }
}
