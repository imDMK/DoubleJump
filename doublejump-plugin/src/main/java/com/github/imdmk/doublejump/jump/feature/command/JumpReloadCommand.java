package com.github.imdmk.doublejump.jump.feature.command;

import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Command(name = "doublejump reload")
@Permission("command.doublejump.reload")
public class JumpReloadCommand {

    @Inject private Logger logger;
    @Inject private ConfigurationManager configurationManager;
    @Inject private MessageService messageService;

    @Execute
    void reload(@Context CommandSender sender) {
        this.configurationManager.reloadAll()
                .thenAccept(v -> this.messageService.send(sender, notice -> notice.reload))
                .exceptionally(throwable -> {
                    this.messageService.send(sender, notice -> notice.reloadError);
                    this.logger.log(Level.SEVERE, "Failed to reload plugin configuration.", throwable);
                    return null;
                });
    }
}
