package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class DoubleJumpListener implements Listener {

    private final JumpSettings jumpSettings;
    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;

    public DoubleJumpListener(JumpSettings jumpSettings, NotificationSettings notificationSettings, NotificationSender notificationSender) {
        this.jumpSettings = jumpSettings;
        this.notificationSettings = notificationSettings;
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
                .multiply(this.jumpSettings.jumpMultiple)
                .setY(this.jumpSettings.jumpUp);

        player.setVelocity(vector);

        if (this.jumpSettings.delaySettings.enabled) {
            jumpPlayer.addDelay(this.jumpSettings.delaySettings.delay);
        }

        if (this.jumpSettings.soundSettings.enabled) {
            this.jumpSettings.soundSettings.sounds.forEach(sound -> sound.play(player));
        }

        if (this.jumpSettings.particleSettings.enabled) {
            this.jumpSettings.particleSettings.particles.forEach(jumpParticle -> jumpParticle.spawn(player));
        }

        if (this.jumpSettings.streakSettings.enabled) {
            jumpPlayer.addStreak(1);

            Notification notification = Notification.builder()
                    .fromNotification(this.notificationSettings.jumpStreakIncreaseNotification)
                    .placeholder("{STREAK}", jumpPlayer.getStreak())
                    .build();

            this.notificationSender.send(player, notification);
        }

        if (this.jumpSettings.limitSettings.enabled) {
            jumpPlayer.removeJumps(1);

            if (!this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                jumpPlayer.addJumpRegenerationDelay(this.jumpSettings.limitSettings.regenerationDelay);
            }
        }
    }
}
