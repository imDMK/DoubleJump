package com.github.imdmk.doublejump.core.feature.jump;

import com.github.imdmk.doublejump.core.feature.jump.message.JumpMessageService;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpValidator;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.platform.event.EventCaller;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@Service
public final class JumpExecutor {

    private final FlightService flightService;
    private final JumpValidator jumpValidator;
    private final JumpMessageService jumpMessageService;
    private final EventCaller eventCaller;

    @Inject
    JumpExecutor(
            FlightService flightService,
            JumpValidator jumpValidator,
            JumpMessageService jumpMessageService,
            EventCaller eventCaller
    ) {
        this.jumpValidator = jumpValidator;
        this.flightService = flightService;
        this.jumpMessageService = jumpMessageService;
        this.eventCaller = eventCaller;
    }

    public JumpResult execute(Player player, JumpPlayer jumpPlayer) {
        JumpContext context = new JumpContext(player, jumpPlayer);
        JumpResult result = jumpValidator.validate(context);

        if (result != JumpResult.ALLOWED) {
            jumpMessageService.notify(player, result);
            return result;
        }

        JumpEvent event = eventCaller.callEvent(new JumpEvent(player, jumpPlayer));
        if (event.isCancelled()) {
            return JumpResult.CANCELLED;
        }

        flightService.stopFlying(player);
        flightService.disallowFlight(player);

        return JumpResult.ALLOWED;
    }
}
