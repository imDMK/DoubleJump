package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpSettings;
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

    private final JumpSettings jumpSettings;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpFallDamageListener(JumpSettings jumpSettings, JumpPlayerManager jumpPlayerManager) {
        this.jumpSettings = jumpSettings;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.jumpSettings.jumpFallDamageEnabled) {
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpSettings.jumpFallDamageEnabled) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (this.shouldTakeFallDamage(player)) {
            player.setAllowFlight(false);
        }
    }

    private boolean shouldTakeFallDamage(Player player) {
        if (player.getFallDistance() < 4.0F) {
            return false;
        }

        Location playerLocation = player.getLocation();
        World playerWorld = player.getWorld();

        if (playerLocation.subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
            return false;
        }

        Vector playerVelocity = player.getVelocity();

        Location velocityLocation = playerLocation.add(playerVelocity);
        Block velocityBlock = playerWorld.getBlockAt(velocityLocation);

        return velocityBlock.getType() != Material.AIR;
    }
}
