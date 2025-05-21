package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.result.RestrictionResult;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.github.imdmk.doublejump.util.GameModeUtil;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Command(name = "doublejump")
@Permission("command.doublejump")
public class DoubleJumpCommand {

    private final MessageService messageService;
    private final JumpPlayerCache jumpCache;
    private final PlayerFlyingService flyingService;
    private final JumpRestrictionService jumpRestrictionService;

    private final Map<Boolean, Consumer<Player>> toggleNotifiers;

    public DoubleJumpCommand(
            @NotNull MessageService messageService,
            @NotNull JumpPlayerCache jumpCache,
            @NotNull PlayerFlyingService flyingService,
            @NotNull JumpRestrictionService jumpRestrictionService
    ) {
        this.messageService = Objects.requireNonNull(messageService, "messageService cannot be null");
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
        this.flyingService = Objects.requireNonNull(flyingService, "flyingService cannot be null");
        this.jumpRestrictionService = Objects.requireNonNull(jumpRestrictionService, "flyingRestrictionService cannot be null");

        this.toggleNotifiers = Map.of(
                true,  player -> messageService.send(player, notice -> notice.jumpEnabled),
                false, player -> messageService.send(player, notice -> notice.jumpDisabled)
        );
    }

    @Execute
    void toggle(@Context Player player) {
        this.jumpCache.get(player.getUniqueId()).ifPresentOrElse(jumpPlayer -> {
            RestrictionResult result = this.jumpRestrictionService.checkAllRestrictions(player);
            if (result.failure()) {
                result.reason().ifPresent(reason -> reason.notify(player, this.messageService));
                return;
            }

            this.toggleJump(player, jumpPlayer);

        }, () -> this.messageService.send(player, notice -> notice.errorOccurred));
    }

    /**
     * Toggles the player's double-jump state, manages flight accordingly,
     * and dispatches the correct notification based on the new state.
     *
     * @param player     the player whose state is toggled
     * @param jumpPlayer the JumpPlayer model for this player
     */
    private void toggleJump(@NotNull Player player, @NotNull JumpPlayer jumpPlayer) {
        boolean wasActive = jumpPlayer.isActive();
        boolean newState = !wasActive;

        jumpPlayer.setActive(newState);

        if (wasActive) {
            boolean allowFlight = GameModeUtil.isFlyingGameMode(player.getGameMode());
            this.flyingService.disable(player, allowFlight);
        }
        else {
            this.flyingService.enable(player);
        }

        this.toggleNotifiers.get(newState).accept(player);
    }

}
