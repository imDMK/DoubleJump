package com.github.imdmk.doublejump.core.feature.jump.effect;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public record SoundEffect(Sound sound, float volume, float pitch)
        implements JumpEffect {

    @Override
    public void apply(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    @Override
    public JumpEffectType type() {
        return JumpEffectType.SOUND;
    }
}
