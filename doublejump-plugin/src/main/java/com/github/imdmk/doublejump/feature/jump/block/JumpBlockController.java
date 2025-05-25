package com.github.imdmk.doublejump.feature.jump.block;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class JumpBlockController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (!this.jumpConfiguration.jumpBlock.enabled) {
            return;
        }

        this.jumpCache.get(player.getUniqueId())
                .filter(jump -> jump.isActivationType(JumpActivationType.BLOCK))
                .ifPresent(jump -> {
                    if (this.getJumpBlock(this.getBlockBelow(player).getType()).isPresent()) {
                        return;
                    }

                    this.deactivateDoubleJump(player, jump);
                });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpConfiguration.jumpBlock.enabled) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (this.isSameBlockPosition(from, to)) {
            return;
        }

        Location below = player.getLocation().subtract(0, 1, 0);

        this.jumpCache.get(player.getUniqueId())
                .filter(JumpPlayer::isJumpAllowed)
                .ifPresent(jumpPlayer ->
                        this.getJumpBlock(below.getBlock().getType())
                                .ifPresentOrElse(
                                        block -> this.activateJumpBlock(player, jumpPlayer, block),
                                        () -> this.deactivateIfBlockActive(player, jumpPlayer, from, to)
        ));
    }

    /**
     * Activates double jump for the player based on the given jump block.
     */
    private void activateJumpBlock(@NotNull Player player, @NotNull JumpPlayer jump, @NotNull JumpBlock block) {
        if (jump.getJumpVelocity().equals(block.jumpVelocity())) {
            return;
        }

        jump.setActivationType(JumpActivationType.BLOCK);
        jump.setActive(true);
        jump.setJumpVelocity(block.jumpVelocity());

        this.flyingService.enable(player);
    }


    /**
     * Deactivates block-based double jump if conditions are met (e.g., stepping off the block).
     */
    private void deactivateIfBlockActive(@NotNull Player player, @NotNull JumpPlayer jumpPlayer, @NotNull Location from, @NotNull Location to) {
        if (!jumpPlayer.isActivationType(JumpActivationType.BLOCK)) {
            return;
        }

        Material belowMaterial = this.getBlockBelow(player).getType();
        boolean isJumpBlock = this.getJumpBlock(belowMaterial).isPresent();
        boolean isAir = belowMaterial == Material.AIR;

        boolean isDescending = to.getY() < from.getY();

        if (isDescending && (!isJumpBlock || isAir)) {
            this.deactivateDoubleJump(player, jumpPlayer);
        }
    }

    /**
     * Gets the JumpBlock instance for a material, if configured.
     */
    private Optional<JumpBlock> getJumpBlock(@NotNull Material material) {
        return this.jumpConfiguration.jumpBlock.getJumpBlock(material);
    }

    /**
     * Returns the block directly under the player, or one block further down if first is air.
     */
    private Block getBlockBelow(@NotNull Player player) {
        Location below = player.getLocation().subtract(0, 1, 0);
        Block block = below.getBlock();

        if (block.getType() != Material.AIR) {
            return block;
        }

        return player.getLocation().subtract(0, 2, 0).getBlock();
    }

    /**
     * Disables the jump and resets activation type.
     */
    private void deactivateDoubleJump(@NotNull Player player, @NotNull JumpPlayer jumpPlayer) {
        jumpPlayer.setActive(false);
        jumpPlayer.setJumpAllowed(true);
        jumpPlayer.setActivationType(JumpActivationType.NONE);

        this.flyingService.disable(player);
    }
}
