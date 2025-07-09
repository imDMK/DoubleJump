package com.github.imdmk.doublejump.jump.feature.placeholder;

import com.github.imdmk.doublejump.infrastructure.placeholder.PluginPlaceholder;
import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Placeholder returning remaining cooldown until the player can double jump again.
 * Example: %doublejump_cooldown% -> "3.2s" or "allowed"
 */
public class JumpCooldownPlaceholder extends PluginPlaceholder {

    private static final String PLACEHOLDER_IDENTIFIER = "doublejump-cooldown";

    @Inject private JumpConfig config;
    @Inject private JumpPlayerCache cache;

    @Override
    public @NotNull String getIdentifier() {
        return PLACEHOLDER_IDENTIFIER;
    }

    @Override
    protected @Nullable String onRequestExpansion(@NotNull OfflinePlayer player, @NotNull String params) {
        return this.cache.get(player.getUniqueId())
                .flatMap(jump -> jump.getNextAllowedJump().map(this::formatCooldown))
                .orElse(this.config.placeholders.cooldownExpiredText);
    }

    private String formatCooldown(@NotNull Instant nextJump) {
        return Optional.of(Duration.between(Instant.now(), nextJump))
                .filter(DurationUtil::isValid)
                .map(DurationUtil::format)
                .orElse(this.config.placeholders.cooldownExpiredText);
    }
}
