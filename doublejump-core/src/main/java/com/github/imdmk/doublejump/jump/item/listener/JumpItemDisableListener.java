package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JumpItemDisableListener implements Listener {

    private final JumpItemSettings jumpItemSettings;
    private final JumpItemService jumpItemService;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;

    public JumpItemDisableListener(JumpItemSettings jumpItemSettings, JumpItemService jumpItemService, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService) {
        this.jumpItemSettings = jumpItemSettings;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!this.jumpItemSettings.enabled) {
            return;
        }

        if (this.jumpItemSettings.usageConfiguration.usage != JumpItemUsage.WEAR_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (!this.jumpItemService.isWearing(player)) {
            return;
        }

        this.jumpPlayerService.disable(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (this.jumpItemSettings.dropConfiguration.disableDoubleJumpMode) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        ItemStack itemDrop = event.getItemDrop().getItemStack();

        if (!this.jumpItemService.compare(itemDrop)) {
            return;
        }

        this.jumpPlayerService.disable(player);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (this.jumpItemSettings.usageConfiguration.usage != JumpItemUsage.HOLD_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        Inventory playerInventory = player.getInventory();

        ItemStack previousItem = playerInventory.getItem(event.getPreviousSlot());
        if (previousItem == null) {
            return;
        }

        if (!this.jumpItemService.compare(previousItem)) {
            return;
        }

        this.jumpPlayerService.disable(player);
    }
}
