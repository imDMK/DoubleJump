package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.configuration.MessageConfiguration;
import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
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

        if (!this.jumpConfiguration.delayConfiguration.enabled) {
            jumpPlayer.addDelay(this.jumpConfiguration.delayConfiguration.delay);
        }

        if (this.jumpConfiguration.soundConfiguration.enabled) {
            this.jumpConfiguration.soundConfiguration.sounds.forEach(sound -> sound.play(player));
        }

        if (this.jumpConfiguration.particleConfiguration.enabled) {
            this.jumpConfiguration.particleConfiguration.particles.forEach(jumpParticle -> jumpParticle.spawn(player));
        }

        if (this.jumpConfiguration.streakConfiguration.enabled) {
            jumpPlayer.addStreak(1);

            Notification notification = Notification.builder()
                    .fromNotification(this.messageConfiguration.jumpStreakIncreaseNotification)
                    .placeholder("{STREAK}", jumpPlayer.getStreak())
                    .build();

            this.notificationSender.sendMessage(player, notification);
        }

        if (this.jumpConfiguration.limitConfiguration.enabled) {
            jumpPlayer.removeJumps(1);

            if (!this.jumpConfiguration.limitConfiguration.regenerationDelay.isZero()) {
                jumpPlayer.addJumpRegenerationDelay(this.jumpConfiguration.limitConfiguration.regenerationDelay);
            }
        }
    }
}
