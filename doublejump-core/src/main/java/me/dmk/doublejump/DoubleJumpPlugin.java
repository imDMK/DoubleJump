package me.dmk.doublejump;

import org.bukkit.plugin.java.JavaPlugin;

public class DoubleJumpPlugin extends JavaPlugin {

    private DoubleJump doubleJump;

    @Override
    public void onEnable() {
        this.doubleJump = new DoubleJump(this);
    }

    @Override
    public void onDisable() {
        this.doubleJump.disable();
    }
}
