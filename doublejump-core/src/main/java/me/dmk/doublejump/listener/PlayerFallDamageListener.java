package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final JumpPlayerManager jumpPlayerManager;

    public PlayerFallDamageListener(PluginConfiguration pluginConfiguration, JumpPlayerManager jumpPlayerManager) {
        this.pluginConfiguration = pluginConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.pluginConfiguration.jumpFallDamage) {
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
