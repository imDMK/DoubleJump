package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

public final class FluidRule implements JumpRule {

    private final JumpConfig config;

    @Inject
    FluidRule(JumpConfig config) {
        this.config = config;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        if (!config.blockInFluid) {
            return JumpResult.ALLOWED;
        }

        Player player = context.player();
        if (player.isInWater() || player.isSwimming()) {
            return JumpResult.BLOCKED_BY_FLUID;
        }

        Material type = player.getLocation().getBlock().getType();
        if (type == Material.WATER || type == Material.LAVA) {
            return JumpResult.BLOCKED_BY_FLUID;
        }

        return JumpResult.ALLOWED;
    }
}
