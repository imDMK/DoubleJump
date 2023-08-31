package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.item.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JumpItemDisableListener implements Listener {

    private final JumpItemConfiguration jumpItemConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemDisableListener(JumpItemConfiguration jumpItemConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!this.jumpItemConfiguration.enabled) {
            return;
        }

        if (this.jumpItemConfiguration.usageConfiguration.usage != JumpItemUsage.WEAR_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (!ItemUtil.isWearingItem(player, this.jumpItemConfiguration.item)) {
            this.jumpPlayerManager.disable(player);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (this.jumpItemConfiguration.dropConfiguration.disableDoubleJumpMode) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        ItemStack itemDrop = event.getItemDrop().getItemStack();
        ItemStack jumpItem = this.jumpItemConfiguration.item;
        boolean ignoreEnchants = !this.jumpItemConfiguration.cancelEnchant;
        if (!ItemUtil.compareItem(itemDrop, jumpItem, true, ignoreEnchants)) {
            return;
        }

        this.jumpPlayerManager.disable(player);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (this.jumpItemConfiguration.usageConfiguration.usage != JumpItemUsage.HOLD_ITEM) {
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

        ItemStack jumpItem = this.jumpItemConfiguration.item;
        boolean ignoreEnchants = !this.jumpItemConfiguration.cancelEnchant;
        if (!ItemUtil.compareItem(previousItem, jumpItem, true, ignoreEnchants)) {
            return;
        }

        this.jumpPlayerManager.disable(player);
    }
}
