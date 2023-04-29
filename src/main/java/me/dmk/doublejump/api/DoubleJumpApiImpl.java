package me.dmk.doublejump.api;

import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.entity.Player;

public class DoubleJumpApiImpl implements DoubleJumpApi {

    private final JumpPlayerManager jumpPlayerManager;

    public DoubleJumpApiImpl(JumpPlayerManager jumpPlayerManager) {
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @Override
    public boolean enable(Player player) {
        return this.jumpPlayerManager.enable(player);
    }

    @Override
    public void disable(Player player) {
        this.jumpPlayerManager.disable(player);
    }

    @Override
    public boolean canUseDoubleJump(Player player) {
        return this.jumpPlayerManager.canUseDoubleJump(player);
    }

    @Override
    public boolean isDoubleJumpMode(Player player) {
        return this.jumpPlayerManager.isDoubleJumpMode(player);
    }
}
