package com.github.imdmk.doublejump.jump.feature.item.controller;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.feature.item.JumpItem;
import com.github.imdmk.doublejump.jump.feature.item.JumpItemService;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsage;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsageStrategy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.Arrays;

public class JumpItemInteractController extends PluginListener {

    @Inject private JumpItemService jumpItemService;
    @Inject private ItemUsageStrategy itemUsageStrategy;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(DoubleJumpEvent event) {
        int reduceDurability = this.jumpConfig.item.reduceDurability;

        if (reduceDurability > 0 && event.getJumpPlayer().isActivationType(JumpActivationType.ITEM)) {
            Arrays.stream(event.getPlayer().getInventory().getContents())
                    .filter(item -> this.jumpItemService.isJumpItem(item))
                    .findFirst()
                    .ifPresent(jumpItem -> this.reduceDurability(jumpItem, reduceDurability));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemService.isEnabled()) {
            return;
        }

        if (this.isSameBlockPosition(event.getFrom(), event.getTo())) {
            return;
        }

        this.jumpCache.get(player.getUniqueId())
                .filter(JumpPlayer::isJumpAllowed)
                .filter(jump -> !jump.isActive())
                .filter(jump -> this.itemUsageStrategy.isItemUsed(player))
                .ifPresent(jump -> {
                    jump.setActive(true);
                    jump.setJumpAllowed(true);

                    JumpItem jumpItem = this.jumpItemService.getJumpItem();
                    jump.setActivationType(JumpActivationType.ITEM);
                    jump.setJumpVelocity(jumpItem.jumpVelocity());

                    this.flyingService.enable(player);
                });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    void onItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemService.isEnabled(ItemUsage.CLICK_ITEM)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack clicked = event.getItem();
        if (clicked != null && this.jumpItemService.isJumpItem(clicked)) {
            event.setCancelled(true);

            JumpItem jumpItem = this.jumpItemService.getJumpItem();
            JumpPlayer jumpPlayer = this.jumpCache.getOrThrow(player.getUniqueId());

            this.useDoubleJump(player, jumpPlayer, jumpItem);
        }
    }

    /**
     * Triggers a double jump action for the given player
     * <p>
     * This method fires a {@link DoubleJumpEvent} that can be cancelled
     * by other listeners.
     *
     * @param player the player performing the jump
     * @param jump the {@link JumpPlayer} instance associated with the player
     */
    private void useDoubleJump(@NotNull Player player, @NotNull JumpPlayer jump, @NotNull JumpItem item) {
        this.eventCaller.callEvent(new DoubleJumpEvent(player, jump, item.jumpVelocity()));
    }

    /**
     * Reduces the durability of the given item by the specified amount.
     * <p>
     * If the resulting durability exceeds the item's maximum durability,
     * the item will be destroyed (i.e., its amount will be set to 0).
     * <p>
     * This method assumes the item uses {@link Damageable} metadata and
     * is applicable only to items that can take damage (e.g., tools, weapons, armor).
     *
     * @param item the {@link ItemStack} whose durability should be reduced
     * @param reduceBy the amount of durability to reduce
     */
    private void reduceDurability(@NotNull ItemStack item, int reduceBy) {
        if (!(item.getItemMeta() instanceof Damageable damageable)) {
            return;
        }

        int maxDurability = item.getType().getMaxDurability();

        boolean shouldDestroyItem = (damageable.getDamage() + reduceBy) >= maxDurability;

        if (shouldDestroyItem) {
            item.setAmount(0);
            return;
        }

        damageable.setDamage(damageable.getDamage() + reduceBy);
        item.setDurability((short) damageable.getDamage());
    }
}
