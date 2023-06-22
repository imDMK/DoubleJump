package me.dmk.doublejump.listener;

import me.dmk.doublejump.player.JumpPlayerMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final JumpPlayerMap jumpPlayerMap;

    public PlayerMoveListener(JumpPlayerMap jumpPlayerMap) {
        this.jumpPlayerMap = jumpPlayerMap;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        if (player.getFallDistance() > 0) {
            player.setAllowFlight(false);
            return;
        }

        this.jumpPlayerMap.get(player).ifPresent(jumpPlayer -> {
            if (!jumpPlayer.canUseJump()) {
                return;
            }

            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
        });
    }
}
