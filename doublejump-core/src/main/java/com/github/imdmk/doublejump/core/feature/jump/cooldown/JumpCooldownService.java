package com.github.imdmk.doublejump.core.feature.jump.cooldown;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.injector.subscriber.Subscribe;
import com.github.imdmk.doublejump.core.injector.subscriber.event.DoubleJumpShutdownEvent;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service(priority = ComponentPriority.LOWEST)
public final class JumpCooldownService {

    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Clock clock;

    @Inject
    JumpCooldownService(Clock clock) {
        this.clock = clock;
    }

    public void applyCooldown(Player player, Duration cooldown) {
        if (cooldown.isZero() || cooldown.isNegative()) {
            return;
        }

        long now = clock.millis();
        cooldowns.put(player.getUniqueId(), now + cooldown.toMillis());
    }

    public long getRemainingMillis(UUID playerId) {
        long now = clock.millis();
        long cooldownUntil = cooldowns.getOrDefault(playerId, 0L);

        return Math.max(0, cooldownUntil - now);
    }

    public boolean isOnCooldown(UUID playerId) {
        return getRemainingMillis(playerId) > 0;
    }

    public void reset(UUID playerId) {
        cooldowns.remove(playerId);
    }

    @Subscribe(event = DoubleJumpShutdownEvent.class)
    private void shutdown() {
        cooldowns.clear();
    }
}