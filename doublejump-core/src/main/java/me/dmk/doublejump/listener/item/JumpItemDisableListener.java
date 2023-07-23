package me.dmk.doublejump.listener.item;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.JumpItemUsage;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JumpItemDisableListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemDisableListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!this.jumpConfiguration.jumpItemEnabled) {
            return;
        }

        if (this.jumpConfiguration.jumpItemUsage != JumpItemUsage.WEAR_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (!PlayerUtil.isWearingItem(player, this.jumpConfiguration.jumpItem)) {
            this.jumpPlayerManager.disable(player);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack itemDrop = event.getItemDrop().getItemStack();
        if (!itemDrop.equals(this.jumpConfiguration.jumpItem)) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (this.jumpConfiguration.jumpItemDropCancelJumpMode) {
            this.jumpPlayerManager.disable(player);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (this.jumpConfiguration.jumpItemUsage != JumpItemUsage.HOLD_ITEM) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        Inventory playerInventory = player.getInventory();
        ItemStack previousItem = playerInventory.getItem(event.getPreviousSlot());

        if (previousItem == null || !previousItem.equals(this.jumpConfiguration.jumpItem)) {
            return;
        }

        this.jumpPlayerManager.disable(player);
    }
}
