package com.github.imdmk.doublejump.core.feature.jump.item.listener;

import com.github.imdmk.doublejump.core.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.core.feature.jump.item.config.JumpItemConfig;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.panda_lang.utilities.inject.annotations.Inject;

@PluginListener
final class JumpItemBlockListener implements Listener {

    private static final int ANVIL_REPAIR_SLOT = 2;

    private final JumpItemConfig config;
    private final JumpItemService itemService;

    @Inject
    JumpItemBlockListener(
            JumpItemConfig config,
            JumpItemService itemService
    ) {
        this.config = config;
        this.itemService = itemService;
    }

    @EventHandler(ignoreCancelled = true)
    void onEnchant(EnchantItemEvent event) {
        if (!config.blockEnchanting) {
            return;
        }

        if (itemService.isJumpItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    void onAnvilRepair(InventoryClickEvent event) {
        if (!config.blockRepairing) {
            return;
        }

        Inventory inventory = event.getClickedInventory();
        if (inventory == null || inventory.getType() != InventoryType.ANVIL) {
            return;
        }

        if (event.getSlot() != ANVIL_REPAIR_SLOT) {
            return;
        }

        ItemStack repair = inventory.getItem(0);
        if (itemService.isJumpItem(repair)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    void onItemDrop(PlayerDropItemEvent event) {
        if (!config.blockDrop) {
            return;
        }

        ItemStack drop = event.getItemDrop().getItemStack();
        if (itemService.isJumpItem(drop)) {
            event.setCancelled(true);
        }
    }
}
