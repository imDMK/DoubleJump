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
        ItemStack jumpItem = this.jumpItemConfiguration.item;
        boolean ignoreEnchants = !this.jumpItemConfiguration.cancelEnchant;

        if (!ItemUtil.compareItem(itemDrop, jumpItem, true, ignoreEnchants)) {
            return;
        }

        if (this.jumpItemConfiguration.dropConfiguration.delete) {
            event.setCancelled(true);
            player.getInventory().removeItem(itemDrop);
        }

        if (this.jumpItemConfiguration.dropConfiguration.disableDoubleJumpMode) {
            this.jumpPlayerManager.disable(player);
        }

        if (this.jumpItemConfiguration.dropConfiguration.cancel) {
            event.setCancelled(true);
        }
    }
}
