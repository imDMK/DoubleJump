package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.command.CommandSettings;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpCommandEditor implements CommandEditor {

    private final CommandSettings commandSettings;

    public DoubleJumpCommandEditor(CommandSettings commandSettings) {
        this.commandSettings = commandSettings;
    }

    @Override
    public State edit(State state) {
        state.permission(List.of(this.commandSettings.doubleJumpPermission));
        state.aliases(this.commandSettings.doubleJumpAliases);

        state.editChild("enable-for", s -> s.permission(List.of(this.commandSettings.doubleJumpEnableForPermission)));
        state.editChild("disable-for", s -> s.permission(List.of(this.commandSettings.doubleJumpEnableForPermission)));

        return state;
    }
}
