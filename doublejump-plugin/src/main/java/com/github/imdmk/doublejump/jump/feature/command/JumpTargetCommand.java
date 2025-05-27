package com.github.imdmk.doublejump.jump.feature.command;

import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocityService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

import java.util.Map;
import java.util.function.Consumer;

@Command(name = "doublejump")
@Permission("command.doublejump.target")
public class JumpTargetCommand {

    @Inject private MessageService messageService;
    @Inject private JumpPlayerCache jumpCache;
    @Inject private JumpVelocityService jumpVelocityService;
    @Inject private PlayerFlyingService flyingService;

    private Map<Boolean, Consumer<CommandSender>> toggleNotifiers;

    @PostConstruct
    public void postConstruct() {
        this.toggleNotifiers = Map.of(
                true,  sender -> this.messageService.send(sender, notice -> notice.jumpEnabledTarget),
                false, sender -> this.messageService.send(sender, notice -> notice.jumpDisabledTarget)
        );
    }

    @Execute(name = "enable-for")
    void enableFor(@Context CommandSender sender, @Arg Player target) {
        JumpPlayer jump = this.jumpCache.getOrThrow(target.getUniqueId());
        this.setDoubleJump(sender, target, jump, true);
    }

    @Execute(name = "disable-for")
    void disableFor(@Context CommandSender sender, @Arg Player target) {
        JumpPlayer jump = this.jumpCache.getOrThrow(target.getUniqueId());
        this.setDoubleJump(sender, target, jump, false);
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
        jump.setJumpAllowed(true);

        if (newState) {
            jump.setActivationType(JumpActivationType.COMMAND);
            jump.setJumpVelocity(this.jumpVelocityService.forPlayer(target));

            this.flyingService.enable(target);
        }
        else {
            jump.setActivationType(JumpActivationType.NONE);
            this.flyingService.disable(target);
        }

        this.toggleNotifiers.get(newState).accept(sender);
    }
}
