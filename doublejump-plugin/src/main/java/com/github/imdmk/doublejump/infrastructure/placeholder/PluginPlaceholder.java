package com.github.imdmk.doublejump.infrastructure.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Base class for defining custom PlaceholderAPI expansions in the plugin.
 * <p>
 * To implement a placeholder, subclass this class and override:
 * <ul>
 *     <li>{@link #getIdentifier()}</li>
 *     <li>{@link #onRequestExpansion(OfflinePlayer, String)}</li>
 * </ul>
 * This class handles version/author forwarding and simplifies the request logic.
 */
public abstract class PluginPlaceholder extends PlaceholderExpansion {

    @Inject private Plugin plugin;

    /**
     * Returns the unique identifier for this placeholder.
     * <p>
     * For example, if the identifier is {@code "doublejump"}, then
     * {@code %doublejump_<param>%} will trigger this expansion.
     *
     * @return the identifier of the placeholder (without % signs)
     */
    @Override
    public abstract @NotNull String getIdentifier();

    /**
     * Returns the list of authors as a comma-separated string from plugin.yml.
     *
     * @return the author(s) of the plugin
     */
    @Override
    public final @NotNull String getAuthor() {
        return String.join(", ", this.plugin.getDescription().getAuthors());
    }

    /**
     * Returns the plugin version from plugin.yml.
     *
     * @return the plugin version
     */
    @Override
    public final @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    /**
     * Handles a placeholder request.
     * <p>
     * Subclasses must implement this method to provide custom logic.
     *
     * @param player the player requesting the placeholder (may be offline)
     * @param params the placeholder parameters (the string after the identifier)
     * @return the result of the placeholder, or {@code null} to return nothing
     */
    protected abstract @Nullable String onRequestExpansion(@NotNull OfflinePlayer player, @NotNull String params);

    /**
     * Called internally by PlaceholderAPI when a placeholder is requested via {@link OfflinePlayer}.
     *
     * @param player the offline player requesting the placeholder
     * @param params the parameters after the identifier
     * @return the placeholder result
     */
    @Override
    public final @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return this.onRequestExpansion(player, params);
    }

    /**
     * Called internally by PlaceholderAPI when a placeholder is requested via {@link Player}.
     *
     * @param player the online player requesting the placeholder
     * @param params the parameters after the identifier
     * @return the placeholder result
     */
    @Override
    public final @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return this.onRequestExpansion(player, params);
    }

    /**
     * Ensures this expansion persists across PlaceholderAPI reloads.
     *
     * @return {@code true} to prevent unregistering on reload
     */
    @Override
    public final boolean persist() {
        return true;
    }
}
