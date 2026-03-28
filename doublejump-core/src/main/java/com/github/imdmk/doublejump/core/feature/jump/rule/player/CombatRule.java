package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import com.github.imdmk.doublejump.core.platform.hook.eternalcombat.CombatService;

public final class CombatRule implements JumpRule {

    private final JumpConfig config;
    private final CombatService combatService;

    public CombatRule(JumpConfig config, CombatService combatService) {
        this.config = config;
        this.combatService = combatService;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        if (!config.blockInCombat) {
            return JumpResult.ALLOWED;
        }

        boolean isInCombat = combatService.isInCombat(context.player());
        if (isInCombat) {
            return JumpResult.BLOCKED_BY_COMBAT;
        }

        return JumpResult.ALLOWED;
    }
}
