package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.restriction.FlyingRestrictionService;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
    void enable(@Context Player player) {
        RestrictionResult result = this.flyingRestrictionService.checkAllRestrictions(player);
        if (result.failure()) {
            return;
        }
    }
}
