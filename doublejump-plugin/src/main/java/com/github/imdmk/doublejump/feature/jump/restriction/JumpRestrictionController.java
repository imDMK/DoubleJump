package com.github.imdmk.doublejump.feature.jump.restriction;

import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResultNotifier;
import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.restriction.RestrictionResult;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Handles restrictions related to double jump:
 * - Cancels jump events if restrictions are violated
 * - Disables double jump in runtime if hard restrictions occur (e.g. world change, ping, permissions)
 * - Enables jump on join/world change if allowed
 */
public class JumpRestrictionController extends PluginListener {

    @Inject private RestrictionResultNotifier resultNotifier;

    @EventHandler(priority = EventPriority.LOWEST)
    void onDoubleJump(final DoubleJumpEvent event) {
        this.jumpCache.getActive(event.getPlayer().getUniqueId())
                .ifPresent(result -> this.handleRestrictions(event.getPlayer(), result));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || to.getBlockX() == from.getBlockX()
                && to.getBlockY() == from.getBlockY()
                && to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        this.jumpCache.getActive(event.getPlayer().getUniqueId())
                .ifPresent(result -> this.handleRestrictions(event.getPlayer(), result));
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerJoin(final PlayerJoinEvent event) {
        this.attemptEnableDoubleJump(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        this.attemptEnableDoubleJump(event.getPlayer());
    }

    /**
     * Applies double jump restrictions to the player.
     * Updates jump state, notifies the player if restricted, and disables flight on hard restrictions.
     *
     * @param player the player to evaluate
     * @param jump the player's jump state to update
     */
    private void handleRestrictions(@NotNull Player player, @NotNull JumpPlayer jump) {
        RestrictionResult result = this.restrictionService.checkAllRestrictions(player);

        this.resultNotifier.notify(player, jump, result);

        if (result.failure()) {
            jump.setJumpAllowed(false);

            if (result.isHardRestriction()) {
                jump.setActive(false);
                this.flyingService.disable(player);
            }

            return;
        }

        if (!jump.isJumpAllowed()) {
            jump.setJumpAllowed(true);

            if (!jump.hasLastNotifiedReason()) {
                this.messageService.send(player, n -> n.jumpAvailable);
            }
        }
    }

    /**
     * Attempts to enable double jump for the player if they pass restrictions.
     * @param player the player to enable double jump for
     */
    private void attemptEnableDoubleJump(@NotNull Player player) {
        this.jumpCache.get(player.getUniqueId())
                .filter(jump -> this.shouldEnable(player))
                .ifPresent(jump -> {
                    jump.setJumpAllowed(true);
                    jump.setActive(true);
                    this.flyingService.enable(player);
                });
    }

    /**
     * Determines whether double jump should be enabled for the player.
     * @param player the player to check
     * @return true if double jump should be enabled
     */
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
