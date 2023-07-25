package me.dmk.doublejump.listener.item;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.util.ItemUtil;
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

    private final JumpConfiguration jumpConfiguration;

    public JumpItemActionBlockListener(JumpConfiguration jumpConfiguration) {
        this.jumpConfiguration = jumpConfiguration;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack item = event.getItem();

        if (item.equals(this.jumpConfiguration.jumpItem)) {
            event.setCancelled(true);
        }
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
        if (itemToRepair == null || !ItemUtil.compareItemByIgnoreDurability(itemToRepair, this.jumpConfiguration.jumpItem)) {
            return;
        }

        if (this.jumpConfiguration.jumpItemCancelRepair) {
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

        ItemStack jumpItemClone = new ItemStack(this.jumpConfiguration.jumpItem.clone());

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
