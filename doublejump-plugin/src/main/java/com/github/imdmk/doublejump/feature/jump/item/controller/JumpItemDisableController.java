package com.github.imdmk.doublejump.feature.jump.item.controller;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsage;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsageStrategy;
import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class JumpItemDisableController extends PluginListener {

    @Inject private JumpItemService jumpItemService;
    @Inject private ItemUsageStrategy itemUsageStrategy;

    /**
     * Called when a player closes their inventory. If the player is no longer wearing
     * the jump item, the double jump effect is disabled.
     *
     * @param event the inventory close event
     */
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!this.jumpItemService.isEnabled(ItemUsage.WEAR_ITEM)) {
            return;
        }

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!this.itemUsageStrategy.isItemUsed(player)) {
            this.disableJump(player);
        }
    }

    /**
     * Called when a player changes the item they are holding in their hotbar.
     * If the previous item was a jump item, the effect is disabled.
     *
     * @param event the item held event
     */
    @EventHandler
    public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemService.isEnabled(ItemUsage.HOLD_ITEM)) {
            return;
        }

        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
        if (previousItem != null && !this.jumpItemService.isJumpItem(previousItem)) {
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
