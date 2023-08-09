package me.dmk.doublejump.jump.listener;

import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.jump.JumpConfiguration;
import me.dmk.doublejump.jump.JumpPlayer;
import me.dmk.doublejump.jump.JumpPlayerManager;
import me.dmk.doublejump.jump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.jump.event.reset.JumpStreakResetReason;
import me.dmk.doublejump.notification.NotificationSender;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class JumpStreakResetListener implements Listener {

    private final Server server;
    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpStreakResetListener(Server server, JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.server = server;
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!this.jumpConfiguration.jumpStreakResetOnDeath) {
            return;
        }

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (jumpPlayer.getStreak() == 0) {
            return;
        }

        JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_DEATH);

        this.server.getPluginManager().callEvent(jumpStreakResetEvent);

        if (jumpStreakResetEvent.isCancelled()) {
            return;
        }

        jumpPlayer.setStreak(0);

        this.notificationSender.sendMessage(player, this.messageConfiguration.jumpStreakResetNotification);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpConfiguration.jumpStreakResetOnGround) {
            return;
        }

        if (!player.isOnGround()) {
            return;
        }

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (jumpPlayer.getStreak() == 0) {
            return;
        }

        JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_ON_GROUND);

        this.server.getPluginManager().callEvent(jumpStreakResetEvent);

        if (jumpStreakResetEvent.isCancelled()) {
            return;
        }

        jumpPlayer.setStreak(0);

        this.notificationSender.sendMessage(player, this.messageConfiguration.jumpStreakResetNotification);
    }
}
