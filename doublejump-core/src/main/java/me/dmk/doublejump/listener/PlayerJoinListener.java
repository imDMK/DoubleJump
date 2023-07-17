package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final JumpPlayerManager jumpPlayerManager;
    private final TaskScheduler taskScheduler;

    public PlayerJoinListener(PluginConfiguration pluginConfiguration, JumpPlayerManager jumpPlayerManager, TaskScheduler taskScheduler) {
        this.pluginConfiguration = pluginConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.pluginConfiguration.disabledGameModes.contains(playerGameMode)) {
            return;
        }

        if (this.pluginConfiguration.disabledWorlds.contains(playerWorldName)) {
            return;
        }

        if (this.pluginConfiguration.enableJumpModeOnJoinForPlayers || this.pluginConfiguration.enableJumpModeOnJoinForAdmins && player.isOp()) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.enable(player, true), 40L);
        }
    }
}
