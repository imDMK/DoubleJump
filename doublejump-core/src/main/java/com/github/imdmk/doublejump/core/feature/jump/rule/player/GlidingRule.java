package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

public final class GlidingRule implements JumpRule {

    private final JumpConfig config;

    @Inject
    GlidingRule(JumpConfig config) {
        this.config = config;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        Player player = context.player();
        if (config.blockWhileGliding && player.isGliding()) {
            return JumpResult.BLOCKED_BY_GLIDING;
        }

        return JumpResult.ALLOWED;
    }
}
