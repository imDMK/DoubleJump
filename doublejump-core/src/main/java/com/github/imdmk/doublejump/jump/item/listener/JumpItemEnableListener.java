package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JumpItemEnableListener implements Listener {

    private final JumpItemConfiguration jumpItemConfiguration;
    private final JumpItemService jumpItemService;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;

    public JumpItemEnableListener(JumpItemConfiguration jumpItemConfiguration, JumpItemService jumpItemService, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService) {
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.jumpItemConfiguration.enabled) {
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
