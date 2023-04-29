package me.dmk.doublejump.listener;

import me.dmk.doublejump.DoubleJump;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChangeListener implements Listener {

    private final DoubleJump doubleJump;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerGameModeChangeListener(DoubleJump doubleJump, JumpPlayerManager jumpPlayerManager) {
        this.doubleJump = doubleJump;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayer(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLaterAsynchronously(
                this.doubleJump,
                () -> this.jumpPlayerManager.refresh(player),
                40L
        );
    }
}
