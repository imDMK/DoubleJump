package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.configuration.MessageConfiguration;
import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class DoubleJumpListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;

    public DoubleJumpListener(JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender) {
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDoubleJump(DoubleJumpEvent event) {
        if (event.isCancelled()) {
            return;
        }

        JumpPlayer jumpPlayer = event.getJumpPlayer();

        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        player.setFlying(false);
        player.setAllowFlight(false);

        Vector vector = playerLocation.getDirection()
                .multiply(this.jumpConfiguration.jumpMultiple)
                .setY(this.jumpConfiguration.jumpUp);

        player.setVelocity(vector);

        if (!this.jumpConfiguration.jumpDelay.isNegative()) {
            jumpPlayer.addDelay(this.jumpConfiguration.jumpDelay);
        }

        if (this.jumpConfiguration.jumpSoundsEnabled) {
            PlayerUtil.playSound(player, this.jumpConfiguration.jumpSound, this.jumpConfiguration.jumpSoundVolume, this.jumpConfiguration.jumpSoundPitch);
        }

        if (this.jumpConfiguration.jumpParticlesEnabled) {
            PlayerUtil.spawnParticles(player, this.jumpConfiguration.jumpParticles, this.jumpConfiguration.jumpParticlesCount, this.jumpConfiguration.jumpParticlesOffsetX, this.jumpConfiguration.jumpParticlesOffsetY, this.jumpConfiguration.jumpParticlesOffsetZ, this.jumpConfiguration.jumpParticlesExtra);
        }

        if (this.jumpConfiguration.jumpStreaksEnabled) {
            jumpPlayer.addStreak(1);

            Notification notification = Notification.builder()
                    .fromNotification(this.messageConfiguration.jumpStreakIncreaseNotification)
                    .placeholder("{STREAK}", jumpPlayer.getStreak())
                    .build();

            this.notificationSender.sendMessage(player, notification);
        }

        if (this.jumpConfiguration.jumpsLimitEnabled) {
            jumpPlayer.removeJumps(1);
            jumpPlayer.addJumpRegenerationDelay(this.jumpConfiguration.jumpsRegenerationDelay);
        }
    }
}
