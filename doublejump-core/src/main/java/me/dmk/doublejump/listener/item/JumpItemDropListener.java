package me.dmk.doublejump.listener.item;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class JumpItemDropListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemDropListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ItemStack itemDrop = event.getItemDrop().getItemStack();
        if (!ItemUtil.compareItemByIgnoreDurability(itemDrop, this.jumpConfiguration.jumpItem)) {
            return;
        }

        if (this.jumpConfiguration.jumpItemDropDelete) {
            event.setCancelled(true);
            player.getInventory().removeItem(itemDrop);
        }

        if (this.jumpConfiguration.disableJumpModeAfterUse) {
            this.jumpPlayerManager.disable(player);
        }

        if (this.jumpConfiguration.jumpItemDropCancel) {
            event.setCancelled(true);
        }
    }
}
