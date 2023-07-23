package me.dmk.doublejump.listener;

import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JumpDisableListener implements Listener {

    private final JumpPlayerManager jumpPlayerManager;

    public JumpDisableListener(JumpPlayerManager jumpPlayerManager) {
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.jumpPlayerManager.disable(player);
    }
}
