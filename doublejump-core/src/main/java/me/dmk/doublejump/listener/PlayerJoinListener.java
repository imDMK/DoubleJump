package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
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

        if (this.pluginConfiguration.enableJumpModeOnJoinForAdmins && player.isOp()
                || this.pluginConfiguration.enableJumpModeOnJoinForPlayers && this.jumpPlayerManager.canUseDoubleJump(player)) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.enable(player), 40L);
        }
    }
}
