package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Instant;
import java.util.Optional;

public class JumpRegenerationListener implements Listener {

    private final JumpSettings jumpSettings;
    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpRegenerationListener(JumpSettings jumpSettings, NotificationSettings notificationSettings, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.jumpSettings = jumpSettings;
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
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

        Instant now = Instant.now();
        Instant endOfJumpsRegenerationDelay = jumpPlayer.getEndOfJumpsRegenerationDelay();

        if (now.isBefore(endOfJumpsRegenerationDelay)) {
            return;
        }

        jumpPlayer.addJumps(1);
        jumpPlayer.addJumpRegenerationDelay(this.jumpSettings.limitSettings.regenerationDelay);

        this.notificationSender.send(player, this.notificationSettings.jumpLimitRegenerationNotification);
    }
}
