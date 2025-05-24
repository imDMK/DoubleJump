package com.github.imdmk.doublejump.feature.jump.item.controller;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.panda_lang.utilities.inject.annotations.Inject;

public class JumpItemRestrictionController extends PluginListener {

    @Inject private JumpItemService jumpItemService;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onItemEnchant(final EnchantItemEvent event) {
        if (!this.jumpItemService.isEnabled() || !this.jumpConfiguration.jumpItem.preventEnchant) {
            return;
        }

        if (this.jumpItemService.isJumpItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onItemAnvil(final InventoryClickEvent event) {
        if (!this.jumpItemService.isEnabled() || !this.jumpConfiguration.jumpItem.preventRepair) {
            return;
        }

        Inventory inventory = event.getClickedInventory();
        if (inventory == null || inventory.getType() != InventoryType.ANVIL) {
            return;
        }

        if (event.getSlot() != 2) {
            return;
        }

        ItemStack repair = inventory.getItem(0);
        if (repair != null && this.jumpItemService.isJumpItem(repair)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onItemDrop(final PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemService.isEnabled() || !this.jumpConfiguration.jumpItem.preventDrop) {
            return;
        }

        ItemStack drop = event.getItemDrop().getItemStack();
        if (this.jumpItemService.isJumpItem(drop)) {
            event.setCancelled(true);
            return;
        }

        // Not a jump item -> reset state
        this.jumpCache.ifActive(player.getUniqueId(), jump -> {
            jump.setActive(false);
            jump.setActivationType(JumpActivationType.NONE);
            this.flyingService.disable(player);
        });
    }
}
