package me.dmk.doublejump.event.reset;

import me.dmk.doublejump.player.JumpPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class JumpStreakResetEvent extends Event implements Cancellable {

    private final Player player;
    private final JumpPlayer jumpPlayer;
    private final JumpStreakResetReason jumpStreakResetReason;

    private boolean canceled;

    private static final HandlerList handlerList = new HandlerList();

    public JumpStreakResetEvent(Player player, JumpPlayer jumpPlayer, JumpStreakResetReason jumpStreakResetReason) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
        this.jumpStreakResetReason = jumpStreakResetReason;
    }

    public Player getPlayer() {
        return this.player;
    }

    public JumpPlayer getJumpPlayer() {
        return this.jumpPlayer;
    }

    public JumpStreakResetReason getJumpStreakResetReason() {
        return this.jumpStreakResetReason;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() { //Required for work
        return handlerList;
    }
}
