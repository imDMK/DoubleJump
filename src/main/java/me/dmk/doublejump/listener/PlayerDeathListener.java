package me.dmk.doublejump.listener;

import me.dmk.doublejump.DoubleJump;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetType;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDeathListener implements Listener {

    private final DoubleJump doubleJump;
    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerDeathListener(DoubleJump doubleJump, PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap, JumpPlayerManager jumpPlayerManager) {
        this.doubleJump = doubleJump;
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerMap.get(player);
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (this.pluginConfiguration.isJumpStreakResetOnDeath()) {
            JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetType.ON_DEATH);
            Bukkit.getPluginManager().callEvent(jumpStreakResetEvent);

            if (jumpStreakResetEvent.isCancelled()) {
                return;
            }

            jumpPlayer.resetJumpStreak();

            this.notificationSender.sendMessage(player, this.pluginConfiguration.getJumpStreakResetMessage());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(
                this.doubleJump,
                () -> this.jumpPlayerManager.refresh(player),
                40L
        );
    }
}
