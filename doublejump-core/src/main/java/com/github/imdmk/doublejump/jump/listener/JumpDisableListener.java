package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JumpDisableListener implements Listener {

    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;
    private final TaskScheduler taskScheduler;

    public JumpDisableListener(JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, JumpRestrictionService jumpRestrictionService, TaskScheduler taskScheduler) {
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.jumpRestrictionService = jumpRestrictionService;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerService.disable(player);
        }
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        this.scheduleCheckRestrictions(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        this.scheduleCheckRestrictions(player);
    }

    private void scheduleCheckRestrictions(Player player) {
        this.taskScheduler.runLater(() -> {
            if (this.jumpRestrictionService.isPassedRestrictions(player, true)) {
                this.jumpPlayerService.disable(player);
            }
        }, 20L);
    }
}
