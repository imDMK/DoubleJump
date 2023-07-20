package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.event.reset.JumpStreakResetReason;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDeathListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;
    private final TaskScheduler taskScheduler;

    public PlayerDeathListener(JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager, TaskScheduler taskScheduler) {
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
        this.taskScheduler = taskScheduler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (this.jumpConfiguration.jumpStreakResetOnDeath) {
            if (jumpPlayer.getStreak() == 0) {
                return;
            }

            JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_DEATH);

            Bukkit.getPluginManager().callEvent(jumpStreakResetEvent);
            if (jumpStreakResetEvent.isCancelled()) {
                return;
            }

            jumpPlayer.setStreak(0);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpStreakResetNotification);
        }

        this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.refresh(player), 40L);
    }
}
