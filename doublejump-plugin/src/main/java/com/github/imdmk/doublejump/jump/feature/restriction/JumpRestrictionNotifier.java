package com.github.imdmk.doublejump.jump.feature.restriction;

import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.shared.Formatter;
import com.github.imdmk.doublejump.infrastructure.message.MessageConfig;
import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Handles notifying players about why their double jump was restricted.
 * Sends appropriate messages with formatting based on the restriction reason.
 */
public class JumpRestrictionNotifier {

    @Inject private MessageService messageService;
    @Inject private JumpConfig jumpConfig;

    /**
     * Sends a notification to the player if the restriction applies and
     * avoids duplicate messages for the same reason.
     *
     * @param player the player to notify
     * @param jump   the JumpPlayer instance tracking player state
     * @param result the restriction result containing the reason
     */
    public void notify(@NotNull Player player, @NotNull JumpPlayer jump, @NotNull RestrictionResult result) {
        if (result.success()) {
            jump.setLastNotifiedReason(null);
            return;
        }

        result.reason()
                .filter(reason -> !jump.isSameAsLastNotifiedReason(reason))
                .ifPresent(reason -> {
                    jump.setLastNotifiedReason(reason);
                    this.notify(player, reason, this.buildFormatterForReason(reason));
                });
    }

    /**
     * Notifies the player using the default formatter.
     *
     * @param player the player to notify
     * @param reason the reason for the restriction
     */
    public void notify(@NotNull Player player, @NotNull RestrictionDenyReason reason) {
        this.notify(player, reason, new Formatter());
    }

    /**
     * Notifies the player using a custom formatter.
     *
     * @param player    the player to notify
     * @param reason    the reason for the restriction
     * @param formatter the placeholder formatter
     */
    public void notify(@NotNull Player player, @NotNull RestrictionDenyReason reason, @NotNull Formatter formatter) {
        this.messageService.send(player, this.getNoticeProvider(reason), formatter);
    }

    private @NotNull Formatter buildFormatterForReason(@NotNull RestrictionDenyReason reason) {
        // This switch allows future extension for other reasons needing custom formatting
        return switch (reason) {
            case JUMP_DELAY -> new Formatter()
                    .register("{TIME}", DurationUtil.format(this.jumpConfig.cooldown));
            default -> new Formatter();
        };
    }

    private @NotNull NoticeProvider<MessageConfig> getNoticeProvider(@NotNull RestrictionDenyReason reason) {
        return switch (reason) {
            case WORLD_DISABLED -> n -> n.worldRestricted;
            case REGION_DISABLED -> n -> n.regionRestricted;
            case PERMISSION_REQUIRED -> n -> n.jumpPermissionRequired;
            case GAME_MODE_BLOCKED -> n -> n.gameModeRestricted;
            case JUMP_DELAY -> n -> n.jumpDelay;
            case PLAYER_GLIDING -> n -> n.blockedWhileGliding;
            case PLAYER_LAGGING -> n -> n.playerLagging;
        };
    }
}
