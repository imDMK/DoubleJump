package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.event.reset.JumpStreakResetEvent;
import me.dmk.doublejump.event.reset.JumpStreakResetReason;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {

    private final Server server;
    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerMap jumpPlayerMap;

    public PlayerMoveListener(Server server, PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerMap jumpPlayerMap) {
        this.server = server;
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerMap = jumpPlayerMap;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        this.jumpPlayerMap.get(player).ifPresent(jumpPlayer -> {
            if (this.pluginConfiguration.jumpFallDamageEnabled && this.shouldTakeFallDamage(player)) {
                player.setAllowFlight(false);
                return;
            }

            if (this.pluginConfiguration.jumpStreakResetOnGround && player.isOnGround()) {
                if (jumpPlayer.getStreak() == 0) {
                    return;
                }

                JumpStreakResetEvent jumpStreakResetEvent = new JumpStreakResetEvent(player, jumpPlayer, JumpStreakResetReason.PLAYER_ON_GROUND);

                this.server.getPluginManager().callEvent(jumpStreakResetEvent);

                if (jumpStreakResetEvent.isCancelled()) {
                    return;
                }

                jumpPlayer.setStreak(0);

                this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpStreakResetNotification);
            }

            if (!jumpPlayer.canUseJump()) {
                return;
            }

            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
        });
    }

    private boolean shouldTakeFallDamage(Player player) {
        if (player.getFallDistance() < 4) {
            return false;
        }

        Location playerLocation = player.getLocation();
        World playerWorld = playerLocation.getWorld();

        if (playerWorld == null) {
            return false;
        }

        if (playerLocation.subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
            return false;
        }

        Vector playerVelocity = player.getVelocity();

        Location velocityLocation = playerLocation.add(playerVelocity);
        Block velocityBlock = playerWorld.getBlockAt(velocityLocation);

        return velocityBlock.getType() != Material.AIR;
    }
}
