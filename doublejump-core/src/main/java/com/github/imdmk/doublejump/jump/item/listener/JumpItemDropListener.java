package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class JumpItemDropListener implements Listener {

    private final JumpItemSettings jumpItemSettings;
    private final JumpItemService jumpItemService;
    private final JumpPlayerService jumpPlayerService;

    public JumpItemDropListener(JumpItemSettings jumpItemSettings, JumpItemService jumpItemService, JumpPlayerService jumpPlayerService) {
        this.jumpItemSettings = jumpItemSettings;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerService = jumpPlayerService;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ItemStack itemDrop = event.getItemDrop().getItemStack();

        if (!this.jumpItemService.compare(itemDrop)) {
            return;
        }

        if (this.jumpItemSettings.dropConfiguration.delete) {
            event.setCancelled(true);
            player.getInventory().removeItem(itemDrop);
        }

        if (this.jumpItemSettings.dropConfiguration.disableDoubleJumpMode) {
            this.jumpPlayerService.disable(player);
        }

        if (this.jumpItemSettings.dropConfiguration.cancel) {
            event.setCancelled(true);
        }
    }
}
