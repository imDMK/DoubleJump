package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.configuration.CommandConfiguration;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpCommandEditor implements CommandEditor {

    private final CommandConfiguration commandConfiguration;

    public DoubleJumpCommandEditor(CommandConfiguration commandConfiguration) {
        this.commandConfiguration = commandConfiguration;
    }

    @Override
    public State edit(State state) {
        state.permission(List.of(this.commandConfiguration.doubleJumpPermission));
        state.aliases(this.commandConfiguration.doubleJumpAliases);

        state.editChild("enable-for", s -> s.permission(List.of(this.commandConfiguration.doubleJumpEnableForPermission)));
        state.editChild("disable-for", s -> s.permission(List.of(this.commandConfiguration.doubleJumpEnableForPermission)));

        return state;
    }
}
