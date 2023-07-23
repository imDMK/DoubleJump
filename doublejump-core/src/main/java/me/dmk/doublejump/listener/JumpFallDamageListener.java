package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class JumpFallDamageListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpFallDamageListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpConfiguration.jumpFallDamageEnabled) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (this.shouldTakeFallDamage(player)) {
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.jumpConfiguration.jumpFallDamageEnabled) {
            return;
        }

        if (!(event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        event.setCancelled(true);
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
