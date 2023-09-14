package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.configuration.MessageConfiguration;
import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.notification.NotificationSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Instant;
import java.util.Optional;

public class JumpRegenerationListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpRegenerationListener(JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.jumpConfiguration.limitConfiguration.regenerationDelay.isZero()) {
            return;
        }

        Player player = event.getPlayer();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (jumpPlayer.getJumps() >= jumpPlayer.getJumpsLimit()) {
            return;
        }

        if (Instant.now().isBefore(jumpPlayer.getEndOfJumpsRegenerationDelay())) {
            return;
        }

        jumpPlayer.addJumps(1);
        jumpPlayer.addJumpRegenerationDelay(this.jumpConfiguration.limitConfiguration.regenerationDelay);

        this.notificationSender.send(player, this.messageConfiguration.jumpLimitRegenerationNotification);
    }
}
