package com.github.imdmk.doublejump.jump.feature.command;

import com.github.imdmk.doublejump.infrastructure.gui.GuiManager;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisualService;
import com.github.imdmk.doublejump.jump.feature.visual.gui.JumpVisualGui;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Command(name = "doublejump visual")
@Permission("command.doublejump.visual")
public class JumpVisualCommand {

    @Inject private Logger logger;
    @Inject private JumpVisualService visualService;
    @Inject private GuiManager guiManager;

    @Execute
    void openGui(@Context Player player) {
        this.visualService.getOrCreate(player.getUniqueId())
                .thenAccept(visual -> this.guiManager.openGui(JumpVisualGui.GUI_IDENTIFIER, player, visual))
                .exceptionally(throwable -> {
                    this.logger.log(Level.SEVERE, "An error occurred while opening the gui", throwable);
                    return null;
                });
    }
}
