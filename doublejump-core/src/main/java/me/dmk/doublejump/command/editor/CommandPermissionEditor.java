package me.dmk.doublejump.command.editor;

import dev.rollczi.litecommands.factory.CommandEditor;
import me.dmk.doublejump.configuration.PluginConfiguration;

import java.util.List;

public class CommandPermissionEditor implements CommandEditor {

    private final PluginConfiguration pluginConfiguration;

    public CommandPermissionEditor(PluginConfiguration pluginConfiguration) {
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
