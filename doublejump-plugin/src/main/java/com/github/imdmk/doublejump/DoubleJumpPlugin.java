package com.github.imdmk.doublejump;

import org.bukkit.plugin.java.JavaPlugin;

public class DoubleJumpPlugin extends JavaPlugin {

    /** bStats Metrics service ID for reporting plugin statistics */
    public static final int METRICS_SERVICE_ID = 19387;

    private DoubleJump doubleJump;

    @Override
    public void onEnable() {
        this.doubleJump = new DoubleJump(this);
    }

    @Override
    public void onDisable() {
        this.doubleJump.disable();
        this.doubleJump = null;
    }
}
