package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Optional;

public class JumpEnableListener implements Listener {

    private final Server server;
    private final JumpSettings jumpSettings;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;
    private final TaskScheduler taskScheduler;

    public JumpEnableListener(Server server, JumpSettings jumpSettings, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, JumpRestrictionService jumpRestrictionService, TaskScheduler taskScheduler) {
        this.server = server;
        this.jumpSettings = jumpSettings;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.jumpRestrictionService = jumpRestrictionService;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        event.setCancelled(true);

        player.setFlying(false);
        player.setAllowFlight(false);

        if (this.jumpRestrictionService.isPassedRestrictions(player, true)) {
            return;
        }

        if (this.jumpRestrictionService.isPassedRestrictions(player, jumpPlayer, true)) {
            return;
        }

        DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

        this.server.getPluginManager().callEvent(doubleJumpEvent);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (player.getAllowFlight()) {
            return;
        }

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (this.jumpRestrictionService.isPassedRestrictions(player, jumpPlayer, true)) {
            return;
        }

        player.setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.jumpRestrictionService.isPassedRestrictions(player, false)) {
            return;
        }

        if (this.jumpSettings.enableJumpModeOnJoinForPlayers || this.jumpSettings.enableJumpModeOnJoinForAdmins && player.isOp()) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerService.enable(player, true), 40L);
        }
    }
}
