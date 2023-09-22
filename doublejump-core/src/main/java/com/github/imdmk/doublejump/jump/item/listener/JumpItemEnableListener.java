package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.JumpItemSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JumpItemEnableListener implements Listener {

    private final JumpItemSettings jumpItemSettings;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final JumpItemService jumpItemService;

    public JumpItemEnableListener(JumpItemSettings jumpItemSettings, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, JumpItemService jumpItemService) {
        this.jumpItemSettings = jumpItemSettings;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.jumpItemService = jumpItemService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpItemSettings.enabled) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        if (this.jumpItemService.isCorrectlyUsed(player)) {
            this.jumpPlayerService.enable(player, true);
        }
    }
}
