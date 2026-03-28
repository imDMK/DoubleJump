package com.github.imdmk.doublejump.core.feature.jump;

import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteCommand;
import com.github.imdmk.doublejump.core.message.MessageService;
import com.github.imdmk.doublejump.core.platform.flight.FlightService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@LiteCommand
@Command(name = "doublejump")
@Permission("command.doublejump")
final class JumpCommand {

    private final MessageService messageService;
    private final FlightService flightService;
    private final JumpPlayerRepository jumpRepository;

    @Inject
    JumpCommand(
            MessageService messageService,
            FlightService flightService,
            JumpPlayerRepository jumpRepository
    ) {
        this.messageService = messageService;
        this.flightService = flightService;
        this.jumpRepository = jumpRepository;
    }

    @Execute(name = "enable")
    void enable(@Context Player player) {
        activateCommandJump(player);
    }

    @Execute(name = "disable")
    void disable(@Context Player player) {
        deactivateCommandJump(player);
    }

    @Execute(name = "enable-for")
    @Permission("command.doublejump.target")
    void enableFor(@Context CommandSender sender, @Arg Player target) {
        activateCommandJump(target);
        messageService.send(sender, n -> n.jumpMessages.enabledForTarget());
    }

    @Execute(name = "disable-for")
    @Permission("command.doublejump.target")
    void disableFor(@Context CommandSender sender, @Arg Player target) {
        deactivateCommandJump(target);
        messageService.send(sender, n -> n.jumpMessages.disabledForTarget());
    }

    private void activateCommandJump(Player target) {
        JumpPlayer jumpPlayer = JumpPlayer.create(target.getUniqueId(), JumpActivationType.COMMAND);

        flightService.allowFlight(target);
        jumpRepository.activate(jumpPlayer);

        messageService.send(target, n -> n.jumpMessages.enabled());
    }

    private void deactivateCommandJump(Player target) {
        flightService.refreshFlightState(target);
        jumpRepository.deactivate(target.getUniqueId());

        messageService.send(target, n -> n.jumpMessages.disabled());
    }
}
