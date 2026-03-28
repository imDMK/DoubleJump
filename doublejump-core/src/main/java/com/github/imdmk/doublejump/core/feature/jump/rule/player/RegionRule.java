package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import com.github.imdmk.doublejump.core.platform.hook.worldguard.RegionService;
import org.panda_lang.utilities.inject.annotations.Inject;

public final class RegionRule implements JumpRule {

    private final JumpConfig config;
    private final RegionService regionService;

    @Inject
    RegionRule(JumpConfig config, RegionService regionService) {
        this.config = config;
        this.regionService = regionService;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        if (config.blockedRegions.isEmpty()) {
            return JumpResult.ALLOWED;
        }

        return regionService.getRegions(context.player())
                .stream()
                .anyMatch(config.blockedRegions::contains)
                ? JumpResult.BLOCKED_BY_REGION
                : JumpResult.ALLOWED;
    }
}
