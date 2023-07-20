package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageListener implements Listener {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerFallDamageListener(JumpConfiguration jumpConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.jumpConfiguration.jumpFallDamageEnabled) {
            return;
        }

        if (!(event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
            return;
        }

        event.setCancelled(true);
    }
}
