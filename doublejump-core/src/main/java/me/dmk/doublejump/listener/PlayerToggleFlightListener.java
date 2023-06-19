package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.DoubleJumpEvent;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerMap;
import me.dmk.doublejump.util.PlayerUtil;
import me.dmk.doublejump.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Optional;

public class PlayerToggleFlightListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;

    public PlayerToggleFlightListener(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();
        Location playerLocation = player.getLocation();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerMap.get(player);
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

        Bukkit.getPluginManager().callEvent(doubleJumpEvent);
        if (doubleJumpEvent.isCancelled()) {
            return;
        }

        event.setCancelled(true);
        player.setFlying(false);

        if (this.pluginConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.jumpPlayerMap.remove(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.pluginConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            this.jumpPlayerMap.remove(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        if (!jumpPlayer.canUseJump()) {
            this.notificationSender.builder()
                    .fromNotification(this.pluginConfiguration.jumpModeDelayNotification)
                    .placeholder("{time}", TimeUtil.instantToString(jumpPlayer.getDelay()))
                    .send(player);
            return;
        }

        Vector vector = playerLocation.getDirection()
                .multiply(this.pluginConfiguration.jumpMultiple)
                .setY(this.pluginConfiguration.jumpUp);

        player.setVelocity(vector);

        int jumpDelay = this.pluginConfiguration.jumpDelay;
        if (jumpDelay > 0) {
            jumpPlayer.addDelay(Duration.ofMillis(jumpDelay));
        }

        jumpPlayer.increaseStreak();

        PlayerUtil.playSound(player, this.pluginConfiguration.jumpSound, this.pluginConfiguration.jumpSoundVolume, this.pluginConfiguration.jumpSoundPitch);
        PlayerUtil.spawnParticles(player, this.pluginConfiguration.jumpParticles, this.pluginConfiguration.jumpParticlesCount, this.pluginConfiguration.jumpParticlesOffsetX, this.pluginConfiguration.jumpParticlesOffsetY, this.pluginConfiguration.jumpParticlesOffsetZ, this.pluginConfiguration.jumpParticlesExtra);

        this.notificationSender.builder()
                .fromNotification(this.pluginConfiguration.jumpStreakIncreaseNotification)
                .placeholder("{streak}", jumpPlayer.getStreak())
                .send(player);
    }
}
