package com.github.imdmk.doublejump.core.feature.jump.falldamage;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpPlayerRepository;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

@PluginListener
final class JumpFallDamageProtectionListener implements Listener {

    private final JumpConfig config;
    private final JumpPlayerRepository jumpRepository;

    @Inject
    JumpFallDamageProtectionListener(
            JumpConfig config,
            JumpPlayerRepository jumpRepository
    ) {
        this.config = config;
        this.jumpRepository = jumpRepository;
    }

    @EventHandler(ignoreCancelled = true)
    void onDamage(EntityDamageEvent event) {
        if (config.enableFallDamage) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!jumpRepository.isActive(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        event.setDamage(0);
    }
}
