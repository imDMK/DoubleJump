package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.item.JumpItemConfiguration;
import com.github.imdmk.doublejump.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class JumpItemDropListener implements Listener {

    private final JumpItemConfiguration jumpItemConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemDropListener(JumpItemConfiguration jumpItemConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ItemStack itemDrop = event.getItemDrop().getItemStack();
        ItemStack jumpItem = this.jumpItemConfiguration.jumpItem;
        boolean ignoreEnchants = !this.jumpItemConfiguration.jumpItemCancelEnchant;

        if (!ItemUtil.compareItem(itemDrop, jumpItem, true, ignoreEnchants)) {
            return;
        }

        if (this.jumpItemConfiguration.jumpItemDropDelete) {
            event.setCancelled(true);
            player.getInventory().removeItem(itemDrop);
        }

        if (this.jumpItemConfiguration.jumpItemDropCancelJumpMode) {
            this.jumpPlayerManager.disable(player);
        }

        if (this.jumpItemConfiguration.jumpItemDropCancel) {
            event.setCancelled(true);
        }
    }
}
