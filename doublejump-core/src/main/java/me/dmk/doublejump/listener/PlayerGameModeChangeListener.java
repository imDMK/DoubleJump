package me.dmk.doublejump.listener;

import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChangeListener implements Listener {

    private final JumpPlayerManager jumpPlayerManager;
    private final TaskScheduler taskScheduler;

    public PlayerGameModeChangeListener(JumpPlayerManager jumpPlayerManager, TaskScheduler taskScheduler) {
        this.jumpPlayerManager = jumpPlayerManager;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayer(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.refresh(player), 40L);
    }
}