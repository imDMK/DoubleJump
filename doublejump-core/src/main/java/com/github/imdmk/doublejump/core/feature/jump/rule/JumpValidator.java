package com.github.imdmk.doublejump.core.feature.jump.rule;

import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.cooldown.CooldownRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.CombatRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.FluidRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.GameModeRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.GlidingRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.RegionRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.VehicleRule;
import com.github.imdmk.doublejump.core.feature.jump.rule.player.WorldRule;
import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.panda_lang.utilities.inject.Injector;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.List;

@Service(priority = ComponentPriority.LOWEST, order = 3)
public final class JumpValidator {

    private final List<JumpRule> rules;

    @Inject
    public JumpValidator(Injector injector) {
        this.rules = List.of(
                injector.newInstance(CombatRule.class),
                injector.newInstance(CooldownRule.class),
                injector.newInstance(WorldRule.class),
                injector.newInstance(GameModeRule.class),
                injector.newInstance(GlidingRule.class),
                injector.newInstance(RegionRule.class),
                injector.newInstance(FluidRule.class),
                injector.newInstance(VehicleRule.class)
        );
    }

    public JumpResult validate(JumpContext context) {
        for (JumpRule rule : rules) {
            JumpResult result = rule.apply(context);
            if (!result.isAllowed()) {
                return result;
            }
        }

        return JumpResult.ALLOWED;
    }
}
