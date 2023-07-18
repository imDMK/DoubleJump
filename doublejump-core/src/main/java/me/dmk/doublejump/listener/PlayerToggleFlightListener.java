package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
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

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerToggleFlightListener(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.pluginConfiguration = pluginConfiguration;
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

        if (this.pluginConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.pluginConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledWorldNotification);
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
                .multiply(this.pluginConfiguration.jumpMultiple)
                .setY(this.pluginConfiguration.jumpUp);

        player.setVelocity(vector);

        if (this.pluginConfiguration.jumpDelayEnabled) {
            jumpPlayer.addDelay(this.pluginConfiguration.jumpDelay);
        }

        if (this.pluginConfiguration.jumpSoundsEnabled) {
            PlayerUtil.playSound(player, this.pluginConfiguration.jumpSound, this.pluginConfiguration.jumpSoundVolume, this.pluginConfiguration.jumpSoundPitch);
        }

        if (this.pluginConfiguration.jumpParticlesEnabled) {
            PlayerUtil.spawnParticles(player, this.pluginConfiguration.jumpParticles, this.pluginConfiguration.jumpParticlesCount, this.pluginConfiguration.jumpParticlesOffsetX, this.pluginConfiguration.jumpParticlesOffsetY, this.pluginConfiguration.jumpParticlesOffsetZ, this.pluginConfiguration.jumpParticlesExtra);
        }

        if (this.pluginConfiguration.jumpStreaksEnabled) {
            jumpPlayer.increaseStreak();

            Notification notification = Notification.builder()
                    .fromNotification(this.pluginConfiguration.jumpStreakIncreaseNotification)
                    .placeholder("{streak}", jumpPlayer.getStreak())
                    .build();

            this.notificationSender.sendMessage(player, notification);
        }
    }
}
