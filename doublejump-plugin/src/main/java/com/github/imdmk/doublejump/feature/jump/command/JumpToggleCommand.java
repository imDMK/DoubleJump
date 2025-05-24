package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
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
public class JumpToggleCommand {

    private final MessageService messageService;
    private final JumpPlayerCache jumpCache;
    private final PlayerFlyingService flyingService;

    private final Map<Boolean, Consumer<Player>> toggleNotifiers;

    public JumpToggleCommand(
            @NotNull MessageService messageService,
            @NotNull JumpPlayerCache jumpCache,
            @NotNull PlayerFlyingService flyingService
    ) {
        this.messageService = Objects.requireNonNull(messageService, "messageService cannot be null");
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
        this.flyingService = Objects.requireNonNull(flyingService, "flyingService cannot be null");

        this.toggleNotifiers = Map.of(
                true,  player -> messageService.send(player, notice -> notice.jumpEnabled),
                false, player -> messageService.send(player, notice -> notice.jumpDisabled)
        );
    }

    @Execute
    void toggle(@Context Player player) {
        this.jumpCache.get(player.getUniqueId()).ifPresentOrElse(jump ->
                        this.toggleJump(player, jump),
                () -> this.messageService.send(player, notice -> notice.errorOccurred));
    }

    /**
     * Toggles the player's double-jump state, manages flight accordingly,
     * and dispatches the correct notification based on the new state.
     *
     * @param player     the player whose state is toggled
     * @param jump the JumpPlayer model for this player
     */
    private void toggleJump(@NotNull Player player, @NotNull JumpPlayer jump) {
        boolean wasActive = jump.isActive();
        boolean newState = !wasActive;

        jump.setActive(newState);
        jump.setJumpAllowed(true);

        if (wasActive) {
            jump.setActivationType(JumpActivationType.NONE);
            this.flyingService.disable(player);
        }
        else {
            jump.setActivationType(JumpActivationType.MANUAL);
            this.flyingService.enable(player);
        }

        this.toggleNotifiers.get(newState).accept(player);
    }

}
