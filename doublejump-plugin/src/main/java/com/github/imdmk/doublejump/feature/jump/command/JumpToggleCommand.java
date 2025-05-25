package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.properties.JumpVelocityService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

import java.util.Map;
import java.util.function.Consumer;

@Command(name = "doublejump")
@Permission("command.doublejump")
public class JumpToggleCommand {

    @Inject private MessageService messageService;
    @Inject private JumpPlayerCache jumpCache;
    @Inject private JumpVelocityService jumpVelocityService;
    @Inject private PlayerFlyingService flyingService;

    private Map<Boolean, Consumer<Player>> toggleNotifiers;

    @PostConstruct
    public void postConstruct() {
        this.toggleNotifiers = Map.of(
                true,  player -> this.messageService.send(player, notice -> notice.jumpEnabled),
                false, player -> this.messageService.send(player, notice -> notice.jumpDisabled)
        );
    }

    @Execute
    void toggle(@Context Player player) {
        this.toggleJump(player, this.jumpCache.getOrThrow(player.getUniqueId()));
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
            jump.setActivationType(JumpActivationType.COMMAND);
            jump.setJumpVelocity(this.jumpVelocityService.forPlayer(player));

            this.flyingService.enable(player);
        }

        this.toggleNotifiers.get(newState).accept(player);
    }

}
