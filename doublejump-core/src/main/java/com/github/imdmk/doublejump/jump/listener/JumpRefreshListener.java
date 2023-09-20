package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class JumpRefreshListener implements Listener {

    private final JumpPlayerService jumpPlayerService;
    private final TaskScheduler taskScheduler;

    public JumpRefreshListener(JumpPlayerService jumpPlayerService, TaskScheduler taskScheduler) {
        this.jumpPlayerService = jumpPlayerService;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        this.taskScheduler.runLaterAsync(() -> this.jumpPlayerService.refresh(player), 40L);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        this.taskScheduler.runLaterAsync(() -> this.jumpPlayerService.refresh(player), 40L);
    }
}
