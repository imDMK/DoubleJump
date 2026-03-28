package com.github.imdmk.doublejump.core.feature.jump.effect;

import org.bukkit.entity.Player;

public interface JumpEffect {

    void apply(Player player);

    JumpEffectType type();

}
