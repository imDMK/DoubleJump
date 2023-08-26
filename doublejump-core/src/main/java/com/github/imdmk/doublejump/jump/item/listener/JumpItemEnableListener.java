package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.item.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class JumpItemEnableListener implements Listener {

    private final JumpItemConfiguration jumpItemConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemEnableListener(JumpItemConfiguration jumpItemConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpItemConfiguration.jumpItemEnabled) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        ItemStack jumpItem = this.jumpItemConfiguration.jumpItem;
        JumpItemUsage jumpItemUsage = this.jumpItemConfiguration.jumpItemUsage;

        if (ItemUtil.isCorrectlyUsed(player, jumpItem, jumpItemUsage)) {
            this.jumpPlayerManager.enable(player, false);
        }
    }
}
