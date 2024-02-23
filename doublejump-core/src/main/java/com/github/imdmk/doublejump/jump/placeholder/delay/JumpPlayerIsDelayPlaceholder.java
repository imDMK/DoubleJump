package com.github.imdmk.doublejump.jump.placeholder.delay;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.placeholder.PlaceholderSettings;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JumpPlayerIsDelayPlaceholder extends PlaceholderExpansion {

    private final PluginDescriptionFile pluginDescriptionFile;
    private final PlaceholderSettings placeholderSettings;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpPlayerIsDelayPlaceholder(PluginDescriptionFile pluginDescriptionFile, PlaceholderSettings placeholderSettings, JumpPlayerManager jumpPlayerManager) {
        this.pluginDescriptionFile = pluginDescriptionFile;
        this.placeholderSettings = placeholderSettings;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "jump-player-is-delay";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", this.pluginDescriptionFile.getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return this.pluginDescriptionFile.getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());

        return jumpPlayerOptional
                .map(JumpPlayer::isDelay)
                .map(aBoolean -> aBoolean ? this.placeholderSettings.whenTrueMessage : this.placeholderSettings.whenFalseMessage)
                .orElse(null);
    }
}
