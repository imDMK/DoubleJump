package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.bukkit.GameMode;
import org.panda_lang.utilities.inject.annotations.Inject;

public final class GameModeRule implements JumpRule {

    private final JumpConfig config;

    @Inject
    GameModeRule(JumpConfig config) {
        this.config = config;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        GameMode playerMode = context.player().getGameMode();
        if (config.blockedGameModes.contains(playerMode)) {
            return JumpResult.BLOCKED_BY_GAMEMODE;
        }

        return JumpResult.ALLOWED;
    }
}
