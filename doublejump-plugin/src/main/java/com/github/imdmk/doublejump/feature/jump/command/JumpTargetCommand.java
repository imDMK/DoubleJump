package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Command(name = "doublejump")
public class JumpTargetCommand {

    private final MessageService messageService;
    private final JumpPlayerCache jumpCache;
    private final PlayerFlyingService flyingService;

    private final Map<Boolean, Consumer<CommandSender>> toggleNotifiers;

    public JumpTargetCommand(
            @NotNull MessageService messageService,
            @NotNull JumpPlayerCache jumpCache,
            @NotNull PlayerFlyingService flyingService
    ) {
        this.messageService = Objects.requireNonNull(messageService, "messageService cannot be null");
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
        this.flyingService = Objects.requireNonNull(flyingService, "flyingService cannot be null");

        this.toggleNotifiers = Map.of(
                true,  sender -> messageService.send(sender, notice -> notice.jumpEnabledTarget),
                false, sender -> messageService.send(sender, notice -> notice.jumpDisabledTarget)
        );
    }

    @Execute(name = "enable-for")
    void enableFor(@Context CommandSender sender, @Arg Player target) {
        this.jumpCache.get(target.getUniqueId()).ifPresentOrElse(jump ->
                        this.setDoubleJump(sender, target, jump, true),
                () -> this.messageService.send(sender, notice -> notice.errorOccurred));
    }

    @Execute(name = "disable-for")
    void disableFor(@Context CommandSender sender, @Arg Player target) {
        this.jumpCache.get(target.getUniqueId()).ifPresentOrElse(jump ->
                        this.setDoubleJump(sender, target, jump, false),
                () -> this.messageService.send(sender, notice -> notice.errorOccurred));
    }

    /**
     * Sets the double jump state for a player and updates related flight settings.
     *
     * @param target   the target player
     * @param jump     the player's jump data
     * @param newState {@code true} to enable double jump, {@code false} to disable
     */
    private void setDoubleJump(@NotNull CommandSender sender, @NotNull Player target, @NotNull JumpPlayer jump, boolean newState) {
        jump.setActive(newState);

        if (newState) {
            jump.setActivationType(JumpActivationType.MANUAL);
            this.flyingService.enable(target);
        }
        else {
            jump.setActivationType(JumpActivationType.NONE);
            this.flyingService.disable(target);
        }

        this.toggleNotifiers.get(newState).accept(sender);
    }
}
