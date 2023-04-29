package me.dmk.doublejump.listener;

import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final JumpPlayerManager jumpPlayerManager;

    public PlayerJoinListener(JumpPlayerManager jumpPlayerManager) {
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.jumpPlayerManager.enableOnJoin(player, true);
    }
}
