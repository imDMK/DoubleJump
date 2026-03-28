package com.github.imdmk.doublejump.core.feature.jump.message;

import com.eternalcode.multification.notice.Notice;
import com.github.imdmk.doublejump.core.feature.jump.message.config.JumpMessages;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.EnumMap;
import java.util.Map;

@Service(priority = ComponentPriority.LOW)
final class JumpMessageResolver {

    private final Map<JumpResult, Notice> messages;

    @Inject
    JumpMessageResolver(JumpMessages config) {
        messages = new EnumMap<>(JumpResult.class);

        messages.put(JumpResult.ALLOWED, config.available());
        messages.put(JumpResult.CANCELLED, Notice.empty());
        messages.put(JumpResult.COOLDOWN, config.cooldown());
        messages.put(JumpResult.BLOCKED_BY_COMBAT, config.blockedByCombat());
        messages.put(JumpResult.BLOCKED_BY_LAG, config.blockedByLag());
        messages.put(JumpResult.BLOCKED_BY_WORLD, config.blockedByWorld());
        messages.put(JumpResult.BLOCKED_BY_REGION, config.blockedByRegion());
        messages.put(JumpResult.BLOCKED_BY_GAMEMODE, config.blockedByGameMode());
        messages.put(JumpResult.BLOCKED_BY_GLIDING, config.blockedByGliding());
        messages.put(JumpResult.BLOCKED_BY_FLUID, config.blockedByFluid());
        messages.put(JumpResult.BLOCKED_BY_VEHICLE, config.blockedByVehicle());
    }

    public Notice resolve(JumpResult result) {
        Notice notice = messages.get(result);
        if (notice == null) {
            throw new IllegalStateException("Missing message for " + result);
        }

        return notice;
    }
}
