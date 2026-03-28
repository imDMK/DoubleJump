package com.github.imdmk.doublejump.core.feature.jump.effect;

import com.github.imdmk.doublejump.core.feature.jump.JumpEvent;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.panda_lang.utilities.inject.annotations.Inject;

@PluginListener
final class JumpEffectListener implements Listener {

    private final JumpEffectService effectService;

    @Inject
    JumpEffectListener(JumpEffectService effectService) {
        this.effectService = effectService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onJump(JumpEvent event) {
        effectService.apply(event.getPlayer());
    }
}
