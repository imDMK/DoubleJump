package me.dmk.doublejump.task;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.event.reset.JumpStreakResetReason;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PlayerGroundTask implements Runnable {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;
    private final Server server;

    public PlayerGroundTask(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap, Server server) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
        this.server = server;
    }

    @Override
    public void run() {
        this.jumpPlayerMap.getJumpPlayers().forEach((uuid, jumpPlayer) -> {
            if (jumpPlayer.getStreak() == 0) { //We don't want to reset the jump streak multiple times
                return;
            }

            Player player = this.server.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                this.jumpPlayerMap.remove(uuid);
                return;
            }

            if (player.isOnGround()) {
                JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_ON_GROUND);

                this.server.getPluginManager().callEvent(jumpStreakResetEvent);

                if (jumpStreakResetEvent.isCancelled()) {
                    return;
                }

                jumpPlayer.setStreak(0);

                this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpStreakResetNotification);
            }
        });
    }
}