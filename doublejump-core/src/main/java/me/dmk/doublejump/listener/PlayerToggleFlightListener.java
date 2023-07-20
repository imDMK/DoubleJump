package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.event.DoubleJumpEvent;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.Optional;

public class PlayerToggleFlightListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerToggleFlightListener(JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();
        Location playerLocation = player.getLocation();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (!jumpPlayer.canUseJump()) {
            return;
        }

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

        Bukkit.getPluginManager().callEvent(doubleJumpEvent);
        if (doubleJumpEvent.isCancelled()) {
            return;
        }

        event.setCancelled(true);

        player.setFlying(false);
        player.setAllowFlight(false);

        Vector vector = playerLocation.getDirection()
                .multiply(this.jumpConfiguration.jumpMultiple)
                .setY(this.jumpConfiguration.jumpUp);

        player.setVelocity(vector);

        if (this.jumpConfiguration.jumpDelayEnabled) {
            jumpPlayer.addDelay(this.jumpConfiguration.jumpDelay);
        }

        if (this.jumpConfiguration.jumpSoundsEnabled) {
            PlayerUtil.playSound(player, this.jumpConfiguration.jumpSound, this.jumpConfiguration.jumpSoundVolume, this.jumpConfiguration.jumpSoundPitch);
        }

        if (this.jumpConfiguration.jumpParticlesEnabled) {
            PlayerUtil.spawnParticles(player, this.jumpConfiguration.jumpParticles, this.jumpConfiguration.jumpParticlesCount, this.jumpConfiguration.jumpParticlesOffsetX, this.jumpConfiguration.jumpParticlesOffsetY, this.jumpConfiguration.jumpParticlesOffsetZ, this.jumpConfiguration.jumpParticlesExtra);
        }

        if (this.jumpConfiguration.jumpStreaksEnabled) {
            jumpPlayer.increaseStreak();

            Notification notification = Notification.builder()
                    .fromNotification(this.messageConfiguration.jumpStreakIncreaseNotification)
                    .placeholder("{streak}", jumpPlayer.getStreak())
                    .build();

            this.notificationSender.sendMessage(player, notification);
        }
    }
}
