package com.github.imdmk.doublejump.core.feature.reload;

import com.github.imdmk.doublejump.core.config.ConfigAccessException;
import com.github.imdmk.doublejump.core.config.ConfigService;
import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteCommand;
import com.github.imdmk.doublejump.core.message.MessageService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.logging.Logger;

@LiteCommand
@Command(name = "doublejump reload")
@Permission("command.doublejump.reload")
final class ReloadCommand {

    private final Logger logger;
    private final ConfigService configService;
    private final MessageService messageService;

    @Inject
    ReloadCommand(
            Logger logger,
            ConfigService configService,
            MessageService messageService
    ) {
        this.logger = logger;
        this.configService = configService;
        this.messageService = messageService;
    }

    @Execute
    void reload(@Context CommandSender sender) {
        try {
            configService.loadAll();
            messageService.send(sender, n -> n.reloadMessages.reloaded());
        } catch (ConfigAccessException e) {
            logger.severe("Failed to reload plugin configuration: " + e.getMessage());
            messageService.send(sender, n -> n.actionExecutionError);
        }
    }
}
