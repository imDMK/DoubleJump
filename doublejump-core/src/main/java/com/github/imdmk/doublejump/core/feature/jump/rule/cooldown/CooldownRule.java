package com.github.imdmk.doublejump.core.feature.jump.rule.cooldown;

import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.cooldown.JumpCooldownService;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;

public final class CooldownRule implements JumpRule {

    private final JumpCooldownService cooldownService;

    @Inject
    CooldownRule(JumpCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        UUID playerId = context.player().getUniqueId();
        if (cooldownService.isOnCooldown(playerId)) {
            return JumpResult.COOLDOWN;
        }

        return JumpResult.ALLOWED;
    }
}
