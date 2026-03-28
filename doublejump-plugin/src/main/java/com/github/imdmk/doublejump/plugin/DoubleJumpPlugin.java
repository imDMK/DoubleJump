package com.github.imdmk.doublejump.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class DoubleJumpPlugin extends JavaPlugin {

    private DoubleJumpCoreWrapper wrapper;

    @Override
    public void onEnable() {
        wrapper = DoubleJumpCoreWrapper.create(getClass().getClassLoader());
        wrapper.enable(this);
    }

    @Override
    public void onDisable() {
        if (wrapper != null) {
            wrapper.disable();
        }
    }
}