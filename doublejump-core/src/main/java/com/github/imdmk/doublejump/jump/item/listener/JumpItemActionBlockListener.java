package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class JumpItemActionBlockListener implements Listener {

    private final JumpItemConfiguration jumpItemConfiguration;
    private final JumpItemService jumpItemService;

    public JumpItemActionBlockListener(JumpItemConfiguration jumpItemConfiguration, JumpItemService jumpItemService) {
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.jumpItemService = jumpItemService;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        if (!this.jumpItemConfiguration.cancelEnchant) {
            return;
        }

        ItemStack item = event.getItem();

        if (!this.jumpItemService.compare(item)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onAnvilInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || clickedInventory.getType() != InventoryType.ANVIL) {
            return;
        }

        int slot = event.getSlot();
        if (slot != 2) {
            return;
        }

        ItemStack itemToRepair = clickedInventory.getItem(0);
        if (itemToRepair == null) {
            return;
        }

        if (!this.jumpItemService.compare(itemToRepair)) {
            return;
        }

        if (this.jumpItemConfiguration.cancelRepair) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }

        ItemStack repairedItem = clickedInventory.getItem(2);
        if (repairedItem == null) {
            return;
        }

        if (!(repairedItem.getItemMeta() instanceof Damageable repairedItemDamageable)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }

        ItemStack jumpItemClone = new ItemStack(this.jumpItemConfiguration.item);

        if (!(jumpItemClone.getItemMeta() instanceof Damageable jumpItemDamageable)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }

        jumpItemDamageable.setDamage(repairedItemDamageable.getDamage());
        jumpItemClone.setItemMeta(jumpItemDamageable);

        event.setCurrentItem(jumpItemClone);
        event.setResult(Event.Result.ALLOW);
    }
}
