package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.PlayerDoubleJumpEvent;
import me.dmk.doublejump.notification.Notification;
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

        //Call double jump event
        PlayerDoubleJumpEvent doubleJumpEvent = new PlayerDoubleJumpEvent(player, jumpPlayer);
        Bukkit.getPluginManager().callEvent(doubleJumpEvent);

        if (doubleJumpEvent.isCancelled()) {
            return;
        }

        //Cancel toggle flight event
        event.setCancelled(true);
        player.setFlying(false);

        if (this.pluginConfiguration.getDisabledGameModes().contains(playerGameMode)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeDisabledGameModeNotification());
            this.jumpPlayerMap.remove(player);
            return;
        }

        if (this.pluginConfiguration.getDisabledWorlds().contains(playerWorld.getName())) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeDisabledWorldNotification());
            this.jumpPlayerMap.remove(player);
            return;
        }

        if (!jumpPlayer.canUseJump()) {
            Notification notification = this.pluginConfiguration.getjumpModeDelayNotification();
            String message = notification.getMessage()
                    .replace("{time}", TimeUtil.instantToString(jumpPlayer.getDelay()));

            this.notificationSender.sendMessage(player, notification.getType(), message);
            return;
        }

        Vector vector = playerLocation.getDirection()
                .multiply(this.pluginConfiguration.getJumpMultiple())
                .setY(this.pluginConfiguration.getJumpUp());

        //Set velocity
        player.setVelocity(vector);

        //Add delay
        int jumpDelay = this.pluginConfiguration.getJumpDelay();
        if (jumpDelay > 0) {
            jumpPlayer.addDelay(jumpDelay);
        }

        //Increase jump streak
        jumpPlayer.increaseJumpStreak();

        //Play sound
        PlayerUtil.playSound(
                player,
                this.pluginConfiguration.getJumpSound(),
                this.pluginConfiguration.getJumpSoundVolume(),
                this.pluginConfiguration.getJumpSoundPitch()
        );

        //Spawn particles
        PlayerUtil.spawnParticles(
                player,
                this.pluginConfiguration.getJumpParticles(),
                this.pluginConfiguration.getJumpParticlesCount(),
                this.pluginConfiguration.getJumpParticlesOffsetX(),
                this.pluginConfiguration.getJumpParticlesOffsetY(),
                this.pluginConfiguration.getJumpParticlesOffsetZ(),
                this.pluginConfiguration.getJumpParticlesExtra()
        );

        //Send notification
        Notification notification = this.pluginConfiguration.getJumpStreakIncreaseMessage();
        String message = notification.getMessage()
                .replace("{streak}", String.valueOf(jumpPlayer.getJumpStreak()));

        this.notificationSender.sendMessage(player, notification.getType(), message);
    }
}
