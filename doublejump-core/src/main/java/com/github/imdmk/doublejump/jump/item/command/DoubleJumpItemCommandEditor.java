package com.github.imdmk.doublejump.jump.item.command;

import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpItemCommandEditor implements CommandEditor {

    private final PluginConfiguration pluginConfiguration;

    public DoubleJumpItemCommandEditor(PluginConfiguration pluginConfiguration) {
        this.pluginConfiguration = pluginConfiguration;
    }

    @Override
    public State edit(State state) {
        state.editChild("give-item", s -> s.permission(List.of(this.pluginConfiguration.doubleJumpGiveItemPermission)));
        state.editChild("remove-item", s -> s.permission(List.of(this.pluginConfiguration.doubleJumpGiveItemPermission)));

        return state;
    }
}