package com.github.imdmk.doublejump.jump.feature.visual.sound;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfig;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class JumpSoundController extends PluginListener {

    @Inject private JumpVisualCache visualCache;
    @Inject private JumpVisualConfig visualConfiguration;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(DoubleJumpEvent event) {
        if (!this.visualConfiguration.sounds.enabled) {
            return;
        }

        Player player = event.getPlayer();

        this.visualCache.getByUuid(player.getUniqueId())
                .ifPresent(visual -> this.playSound(player, visual));
    }

    private void playSound(@NotNull Player player, @NotNull JumpVisual visual) {
        visual.getJumpSound().ifPresent(sound -> sound.play(player));
    }
}
