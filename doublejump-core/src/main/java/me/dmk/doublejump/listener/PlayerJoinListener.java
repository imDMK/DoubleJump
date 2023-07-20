package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;
    private final TaskScheduler taskScheduler;

    public PlayerJoinListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager, TaskScheduler taskScheduler) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorldName)) {
            return;
        }

        if (this.jumpConfiguration.enableJumpModeOnJoinForPlayers || this.jumpConfiguration.enableJumpModeOnJoinForAdmins && player.isOp()) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.enable(player, true), 40L);
        }
    }
}
