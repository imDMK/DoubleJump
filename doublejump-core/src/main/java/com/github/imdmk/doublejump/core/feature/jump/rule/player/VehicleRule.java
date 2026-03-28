package com.github.imdmk.doublejump.core.feature.jump.rule.player;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpContext;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpRule;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

public class VehicleRule implements JumpRule {

    private final JumpConfig config;

    @Inject
    VehicleRule(JumpConfig config) {
        this.config = config;
    }

    @Override
    public JumpResult apply(JumpContext context) {
        if (!config.blockInVehicle) {
            return JumpResult.ALLOWED;
        }

        Player player = context.player();
        if (player.isInsideVehicle()) {
            return JumpResult.BLOCKED_BY_VEHICLE;
        }

        return JumpResult.ALLOWED;
    }
}
