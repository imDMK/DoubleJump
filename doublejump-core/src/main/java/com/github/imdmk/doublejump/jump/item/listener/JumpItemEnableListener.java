package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JumpItemEnableListener implements Listener {

    private final JumpItemSettings jumpItemSettings;
    private final JumpItemService jumpItemService;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;

    public JumpItemEnableListener(JumpItemSettings jumpItemSettings, JumpItemService jumpItemService, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService) {
        this.jumpItemSettings = jumpItemSettings;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
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
