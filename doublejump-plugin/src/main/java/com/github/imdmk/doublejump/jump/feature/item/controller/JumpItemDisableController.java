package com.github.imdmk.doublejump.jump.feature.item.controller;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.feature.item.JumpItemService;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Controller responsible for disabling the double jump functionality
 * when a player removes or changes the jump-enabling item.
 * Listens to inventory and held item change events to manage jump state.
 */
public class JumpItemDisableController extends PluginListener {

    @Inject private JumpItemService jumpItemService;

    /**
     * Handles clicks in the inventory that may change equipment slots.
     * If a player stops wearing a jump item, disables the double jump ability.
     *
     * @param event the inventory click event
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!this.jumpItemService.isEnabled(ItemUsage.WEAR_ITEM)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getInventory().getHolder() instanceof Player)) {
            return;
        }

        int slot = event.getSlot();
        if (!this.isEquipmentSlot(slot)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();

        ItemStack oldItem = inventory.getItem(slot);
        ItemStack newItem = event.getCursor();

        boolean wasWearingJumpItem = oldItem != null && this.jumpItemService.isJumpItem(oldItem);
        boolean willWearJumpItem = newItem != null && this.jumpItemService.isJumpItem(newItem);

        if (wasWearingJumpItem && !willWearJumpItem) {
            this.disableJump(player);
        }
    }

    private boolean isEquipmentSlot(int slot) {
        return switch (slot) {
            case 36, 37, 38, 39, 40, 45 -> true;
            default -> false;
        };
    }

    /**
     * Called when a player changes the item they are holding in their hotbar.
     * If the previous item was a jump item, the effect is disabled.
     *
     * @param event the item held event
     */
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemService.isEnabled(ItemUsage.HOLD_ITEM)) {
            return;
        }

        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
        if (previousItem != null && this.jumpItemService.isJumpItem(previousItem)) {
            this.disableJump(player);
        }
    }

    /**
     * Disables the double jump state for the specified player.
     * This method resets the jump state and disables flying mode if the player is currently active.
     *
     * @param player the player whose jump state should be disabled
     */
    private void disableJump(@NotNull Player player) {
        this.jumpCache.getActive(player.getUniqueId())
                .ifPresent(jump -> {
                    jump.setActive(false);
                    jump.setJumpAllowed(true);
                    jump.setActivationType(JumpActivationType.NONE);
                    this.flyingService.disable(player);
                });
    }

}
