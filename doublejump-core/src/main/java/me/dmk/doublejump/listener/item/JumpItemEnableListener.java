package me.dmk.doublejump.listener.item;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.JumpItemUsage;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class JumpItemEnableListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemEnableListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpConfiguration.jumpItemEnabled) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        ItemStack jumpItem = this.jumpConfiguration.jumpItem;
        JumpItemUsage jumpItemUsage = this.jumpConfiguration.jumpItemUsage;

        if (ItemUtil.isCorrectlyUsed(player, jumpItem, jumpItemUsage)) {
            this.jumpPlayerManager.enable(player, false);
        }
    }
}
