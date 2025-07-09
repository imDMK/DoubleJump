package com.github.imdmk.doublejump.jump.feature.restriction.delay;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

/**
 * Applies a cooldown delay after a successful double jump.
 * Triggered by {@link DoubleJumpEvent}.
 */
public class CooldownRestrictionController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(final DoubleJumpEvent event) {
        JumpPlayer player = event.getJumpPlayer();
        Duration delay = this.jumpConfig.cooldown;

        if (!DurationUtil.isValid(delay)) {
            return;
        }

        player.setNextAllowedJump(this.calculateNextAllowedJump(delay));
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
