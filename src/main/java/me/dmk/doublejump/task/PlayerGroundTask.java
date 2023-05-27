package me.dmk.doublejump.task;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetType;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerGroundTask implements Runnable {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;

    public PlayerGroundTask(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
    }

    @Override
    public void run() {
        this.jumpPlayerMap.getJumpPlayers().forEach((uuid, jumpPlayer) -> {
            if (jumpPlayer.getJumpStreak() == 0) { //We don't want to reset the jump streak multiple times
                return;
            }

            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                this.jumpPlayerMap.remove(uuid);
                return;
            }

            if (player.isOnGround()) {
                JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetType.ON_GROUND);
                Bukkit.getPluginManager().callEvent(jumpStreakResetEvent);

                if (jumpStreakResetEvent.isCancelled()) {
                    return;
                }

                jumpPlayer.resetJumpStreak();

                this.notificationSender.sendMessage(player, this.pluginConfiguration.getJumpStreakResetMessage());
            }
        });
    }
}
