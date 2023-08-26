package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import dev.rollczi.litecommands.factory.CommandEditor;

import java.util.List;

public class DoubleJumpCommandEditor implements CommandEditor {

    private final PluginConfiguration pluginConfiguration;

    public DoubleJumpCommandEditor(PluginConfiguration pluginConfiguration) {
        this.pluginConfiguration = pluginConfiguration;
    }

    @Override
    public State edit(State state) {
        state.permission(List.of(this.pluginConfiguration.doubleJumpCommandPermission));

        state.editChild("enable-for", s -> s.permission(List.of(this.pluginConfiguration.doubleJumpEnableForPermission)));
        state.editChild("disable-for", s -> s.permission(List.of(this.pluginConfiguration.doubleJumpEnableForPermission)));

        return state;
    }
}
