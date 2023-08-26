package me.dmk.doublejump.jump.item.listener;

import me.dmk.doublejump.jump.JumpPlayerManager;
import me.dmk.doublejump.jump.item.JumpItemConfiguration;
import me.dmk.doublejump.jump.item.JumpItemUsage;
import me.dmk.doublejump.util.ItemUtil;
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

        if (!this.jumpItemConfiguration.jumpItemEnabled) {
            return;
        }

        if (this.jumpItemConfiguration.jumpItemUsage != JumpItemUsage.WEAR_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (!ItemUtil.isWearingItem(player, this.jumpItemConfiguration.jumpItem)) {
            this.jumpPlayerManager.disable(player);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (this.jumpItemConfiguration.jumpItemDropCancelJumpMode) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        ItemStack itemDrop = event.getItemDrop().getItemStack();
        ItemStack jumpItem = this.jumpItemConfiguration.jumpItem;
        boolean ignoreEnchants = !this.jumpItemConfiguration.jumpItemCancelEnchant;
        if (!ItemUtil.compareItem(itemDrop, jumpItem, true, ignoreEnchants)) {
            return;
        }

        this.jumpPlayerManager.disable(player);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (this.jumpItemConfiguration.jumpItemUsage != JumpItemUsage.HOLD_ITEM) {
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

        ItemStack jumpItem = this.jumpItemConfiguration.jumpItem;
        boolean ignoreEnchants = !this.jumpItemConfiguration.jumpItemCancelEnchant;

        if (!ItemUtil.compareItem(previousItem, jumpItem, true, ignoreEnchants)) {
            return;
        }

        this.jumpPlayerManager.disable(player);
    }
}
