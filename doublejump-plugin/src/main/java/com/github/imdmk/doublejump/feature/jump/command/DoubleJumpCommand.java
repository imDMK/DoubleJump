package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.restriction.FlyingRestrictionService;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult;
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

import java.util.Objects;
import java.util.Optional;

@Command(name = "doublejump")
@Permission("command.doublejump")
public class DoubleJumpCommand {

    private final MessageService messageService;
    private final JumpPlayerCache jumpCache;
    private final PlayerFlyingService flyingService;
    private final FlyingRestrictionService flyingRestrictionService;

    public DoubleJumpCommand(
            @NotNull MessageService messageService,
            @NotNull JumpPlayerCache jumpCache,
            @NotNull PlayerFlyingService flyingService,
            @NotNull FlyingRestrictionService flyingRestrictionService
    ) {
        this.messageService = Objects.requireNonNull(messageService, "messageService cannot be null");
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
        this.flyingService = Objects.requireNonNull(flyingService, "flyingService cannot be null");
        this.flyingRestrictionService = Objects.requireNonNull(flyingRestrictionService, "flyingRestrictionService cannot be null");
    }

    @Execute
    void toggle(@Context Player player) {
        Optional<JumpPlayer> optionalJumpPlayer = this.jumpCache.get(player.getUniqueId());
        if (optionalJumpPlayer.isEmpty()) {
            this.messageService.send(player, notice -> notice.errorOccurred);
            return;
        }

        JumpPlayer jumpPlayer = optionalJumpPlayer.get();

        RestrictionResult result = this.flyingRestrictionService.checkAllRestrictions(player);
        if (result.failure()) {
            result.reason().ifPresent(reason -> this.notifyRestrictionReason(player, reason));
            return;
        }

        this.toggleJump(player, jumpPlayer);
    }

    private void toggleJump(@NotNull Player player, @NotNull JumpPlayer jumpPlayer) {
        if (jumpPlayer.isActive()) {
            jumpPlayer.setActive(false);

            boolean allowFlight = GameModeUtil.isFlyingGameMode(player.getGameMode());
            this.flyingService.disable(player, allowFlight);
        }
        else {
            jumpPlayer.setActive(true);
            this.flyingService.enable(player);
        }
    }

    private void notifyRestrictionReason(@NotNull Player player, @NotNull RestrictionDenyReason reason) {
        switch (reason) {
            case WORLD_DISABLED -> this.messageService.send(player, notice -> notice.worldRestricted);
            case GAME_MODE_BLOCKED -> this.messageService.send(player, notice -> notice.gameModeRestricted);
            case PERMISSION_REQUIRED -> this.messageService.send(player, notice -> notice.jumpPermissionRequired);
        }
    }
}
