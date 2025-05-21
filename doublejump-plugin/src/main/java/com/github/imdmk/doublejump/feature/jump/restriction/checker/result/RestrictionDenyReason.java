package com.github.imdmk.doublejump.feature.jump.restriction.checker.result;

import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.shared.Formatter;
import com.github.imdmk.doublejump.feature.message.MessageConfiguration;
import com.github.imdmk.doublejump.feature.message.MessageService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Enumerates reasons why a player may be restricted from using double jump.
 * <p>
 * These reasons are used in {@link RestrictionResult}
 * to provide context when a restriction check fails.
 * </p>
 */
public enum RestrictionDenyReason {

    /**
     * The player's current world is not allowed for double jump.
     */
    WORLD_DISABLED(true),

    /**
     * The player's current game mode is not permitted for double jump.
     */
    GAME_MODE_BLOCKED(true),

    /**
     * The player lacks the necessary permission to use double jump.
     */
    PERMISSION_REQUIRED(true),

    /**
     * The player's network connection is unstable (ping is too high).
     */
    PLAYER_LAGGING(true),

    /**
     * The player is attempting to double jump before the configured cooldown
     * period has elapsed since the last jump.
     * Hard restriction: false
     */
    JUMP_DELAY(false);

    private final boolean hardRestriction;

    RestrictionDenyReason(boolean hardRestriction) {
        this.hardRestriction = hardRestriction;
    }

    public boolean isHardRestriction() {
        return this.hardRestriction;
    }

    /**
     * Notifies the player about this restriction using the default message format.
     *
     * @param player the player to notify
     * @param messageService the message service to use
     */
    public void notify(@NotNull Player player, @NotNull MessageService messageService) {
        this.notify(player, messageService, new Formatter());
    }

    /**
     * Notifies the player about this restriction using a custom message formatter.
     *
     * @param player the player to notify
     * @param messageService the message service to use
     * @param formatter the placeholder formatter
     */
    public void notify(@NotNull Player player, @NotNull MessageService messageService, @NotNull Formatter formatter) {
        NoticeProvider<MessageConfiguration> notice = switch (this) {
            case WORLD_DISABLED -> n -> n.worldRestricted;
            case PERMISSION_REQUIRED -> n -> n.jumpPermissionRequired;
            case GAME_MODE_BLOCKED -> n -> n.gameModeRestricted;
            case JUMP_DELAY -> n -> n.jumpDelay;
            case PLAYER_LAGGING -> n -> n.playerLagging;
        };

        messageService.send(player, notice, formatter);
    }

}
