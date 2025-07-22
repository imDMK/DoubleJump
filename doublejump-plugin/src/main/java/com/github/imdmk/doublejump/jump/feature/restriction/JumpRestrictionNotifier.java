package com.github.imdmk.doublejump.jump.feature.restriction;

import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.shared.Formatter;
import com.github.imdmk.doublejump.infrastructure.message.MessageConfig;
import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;
import java.time.Instant;

/**
 * Handles sending messages to players when their double jump is restricted.
 */
public class JumpRestrictionNotifier {

    @Inject private MessageService messageService;

    public void notify(@NotNull Player player, @NotNull JumpPlayer jump, @NotNull RestrictionResult result) {
        if (result.success()) {
            jump.setLastNotifiedReason(null);
            return;
        }

        result.reason()
                .filter(reason -> !jump.isSameAsLastNotifiedReason(reason))
                .ifPresent(reason -> {
                    jump.setLastNotifiedReason(reason);
                    this.sendMessage(player, reason, this.buildFormatter(jump, reason));
                });
    }

    public void notify(@NotNull Player player, @NotNull RestrictionDenyReason reason) {
        this.sendMessage(player, reason, new Formatter());
    }

    public void notify(@NotNull Player player, @NotNull RestrictionDenyReason reason, @NotNull Formatter formatter) {
        this.sendMessage(player, reason, formatter);
    }

    private void sendMessage(@NotNull Player player, @NotNull RestrictionDenyReason reason, @NotNull Formatter formatter) {
        this.messageService.send(player, this.resolveNoticeProvider(reason), formatter);
    }

    private @NotNull Formatter buildFormatter(@NotNull JumpPlayer jump, @NotNull RestrictionDenyReason reason) {
        return switch (reason) {
            case JUMP_DELAY -> new Formatter()
                    .register("{TIME}", DurationUtil.format(this.calculateRemainingDelay(jump)));
            default -> new Formatter();
        };
    }

    private @NotNull Duration calculateRemainingDelay(@NotNull JumpPlayer jump) {
        return jump.getNextAllowedJump()
                .map(next -> Duration.between(Instant.now(), next))
                .filter(duration -> !duration.isNegative())
                .orElse(Duration.ZERO);
    }

    private @NotNull NoticeProvider<MessageConfig> resolveNoticeProvider(@NotNull RestrictionDenyReason reason) {
        return switch (reason) {
            case WORLD_DISABLED       -> config -> config.worldRestricted;
            case REGION_DISABLED      -> config -> config.regionRestricted;
            case PERMISSION_REQUIRED  -> config -> config.jumpPermissionRequired;
            case GAME_MODE_BLOCKED    -> config -> config.gameModeRestricted;
            case JUMP_DELAY           -> config -> config.jumpDelay;
            case PLAYER_GLIDING       -> config -> config.blockedWhileGliding;
            case PLAYER_LAGGING       -> config -> config.playerLagging;
        };
    }
}
