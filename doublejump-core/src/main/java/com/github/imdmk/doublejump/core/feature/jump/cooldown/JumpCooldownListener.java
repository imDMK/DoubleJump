package com.github.imdmk.doublejump.core.feature.jump.cooldown;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpEvent;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.shared.permission.PermissionBasedValueProvider;
import com.github.imdmk.doublejump.core.shared.permission.PlayerValueProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;

@PluginListener
final class JumpCooldownListener implements Listener {

    private final JumpCooldownService cooldownService;
    private final PlayerValueProvider<Duration> cooldownProvider;

    @Inject
    JumpCooldownListener(JumpCooldownService cooldownService, JumpConfig config) {
        this.cooldownService = cooldownService;
        this.cooldownProvider = new PermissionBasedValueProvider<>(config.cooldowns);
    }

    @EventHandler(ignoreCancelled = true)
    void onJump(JumpEvent event) {
        Player player = event.getPlayer();
        cooldownService.applyCooldown(player, cooldownProvider.resolve(player));
    }
}
