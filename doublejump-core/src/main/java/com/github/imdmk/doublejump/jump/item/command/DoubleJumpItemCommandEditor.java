package com.github.imdmk.doublejump.jump.item.command;

import com.github.imdmk.doublejump.configuration.CommandConfiguration;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpItemCommandEditor implements CommandEditor {

    private final CommandConfiguration commandConfiguration;

    public DoubleJumpItemCommandEditor(CommandConfiguration commandConfiguration) {
        this.commandConfiguration = commandConfiguration;
    }

    @Override
    public State edit(State state) {
        state.editChild("give-item", s -> s.permission(List.of(this.commandConfiguration.doubleJumpGiveItemPermission)));
        state.editChild("remove-item", s -> s.permission(List.of(this.commandConfiguration.doubleJumpGiveItemPermission)));

        return state;
    }
}