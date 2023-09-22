package com.github.imdmk.doublejump.jump.item.command;

import com.github.imdmk.doublejump.command.CommandSettings;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpItemCommandEditor implements CommandEditor {

    private final CommandSettings commandSettings;

    public DoubleJumpItemCommandEditor(CommandSettings commandSettings) {
        this.commandSettings = commandSettings;
    }

    @Override
    public State edit(State state) {
        state.editChild("give-item", s -> s.permission(List.of(this.commandSettings.doubleJumpItemPermission)));
        state.editChild("remove-item", s -> s.permission(List.of(this.commandSettings.doubleJumpItemPermission)));

        return state;
    }
}