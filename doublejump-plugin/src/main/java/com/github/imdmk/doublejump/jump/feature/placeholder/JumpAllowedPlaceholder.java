package com.github.imdmk.doublejump.jump.feature.placeholder;

import com.github.imdmk.doublejump.infrastructure.placeholder.PluginPlaceholder;
import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.annotations.Inject;

public class JumpAllowedPlaceholder extends PluginPlaceholder {

    private static final String PLACEHOLDER_IDENTIFIER = "doublejump-allowed";

    @Inject private JumpConfig config;
    @Inject private JumpPlayerCache cache;

    @Override
    public @NotNull String getIdentifier() {
        return PLACEHOLDER_IDENTIFIER;
    }

    @Override
    protected @Nullable String onRequestExpansion(@NotNull OfflinePlayer player, @NotNull String params) {
        return this.cache.get(player.getUniqueId())
                .map(this::formatJumpAllowed)
                .orElse(this.config.placeholders.jumpDisabledText);
    }

    private String formatJumpAllowed(@NotNull JumpPlayer jump) {
        return jump.isJumpAllowed() ? this.config.placeholders.jumpAllowedText : this.config.placeholders.jumpNotAllowedText;
    }
}
