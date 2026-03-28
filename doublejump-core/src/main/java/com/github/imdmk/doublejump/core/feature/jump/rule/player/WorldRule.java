package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.panda_lang.utilities.inject.annotations.Inject;

public final class WorldRule implements JumpRule {

    private final JumpConfig config;

    @Inject
    WorldRule(JumpConfig config) {
        this.config = config;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        String world = context.player().getWorld().getName();
        if (config.blockedWorlds.contains(world)) {
            return JumpResult.BLOCKED_BY_WORLD;
        }

        return JumpResult.ALLOWED;
    }
}
