package com.github.imdmk.doublejump.jump.command.editor;

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

        state.editChild("enable-for", s -> s.permission(List.of(this.commandSettings.doubleJumpForPermission)));
        state.editChild("disable-for", s -> s.permission(List.of(this.commandSettings.doubleJumpForPermission)));

        return state;
    }
}
