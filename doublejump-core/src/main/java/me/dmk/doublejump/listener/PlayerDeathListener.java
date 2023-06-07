package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.event.reset.JumpStreakResetReason;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.player.JumpPlayerMap;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDeathListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;
    private final JumpPlayerManager jumpPlayerManager;
    private final TaskScheduler taskScheduler;

    public PlayerDeathListener(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap, JumpPlayerManager jumpPlayerManager, TaskScheduler taskScheduler) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
        this.jumpPlayerManager = jumpPlayerManager;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerMap.get(player);
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (this.pluginConfiguration.jumpStreakResetOnDeath) {
            JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_DEATH);

            Bukkit.getPluginManager().callEvent(jumpStreakResetEvent);
            if (jumpStreakResetEvent.isCancelled()) {
                return;
            }

            jumpPlayer.setStreak(0);

            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpStreakResetNotification);
        }

        this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.refresh(player), 40L);
    }
}
