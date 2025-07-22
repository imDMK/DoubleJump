package com.github.imdmk.doublejump.jump.feature.restriction.cooldown;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;
import java.time.Instant;

/**
 * Applies a cooldown delay after a successful double jump.
 * Triggered by {@link DoubleJumpEvent}.
 */
public class CooldownRestrictionController extends PluginListener {

    @Inject private CooldownRestrictionService cooldownRestrictionService;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(DoubleJumpEvent event) {
        Duration delay = this.cooldownRestrictionService.forPlayer(event.getPlayer());
        if (DurationUtil.isValid(delay)) {
            event.getJumpPlayer().setNextAllowedJump(this.calculateNextAllowedJump(delay));
        }
    }

    /**
     * Calculates the next timestamp when a player is allowed to jump again.
     *
     * @param delay the configured jump cooldown duration
     * @return an Instant indicating when jumping will be allowed again
     */
    @NotNull
    private Instant calculateNextAllowedJump(@NotNull Duration delay) {
        return Instant.now().plus(delay);
    }
}
