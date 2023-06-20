package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.PluginConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageListener implements Listener {

    private final PluginConfiguration pluginConfiguration;

    public PlayerFallDamageListener(PluginConfiguration pluginConfiguration) {
        this.pluginConfiguration = pluginConfiguration;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!this.pluginConfiguration.jumpFallDamage) {
            return;
        }

        if (!(event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        event.setCancelled(true);
    }
}
