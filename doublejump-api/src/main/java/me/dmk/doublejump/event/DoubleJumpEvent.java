package me.dmk.doublejump.event;

import me.dmk.doublejump.player.JumpPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class DoubleJumpEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final JumpPlayer jumpPlayer;

    private boolean canceled;

    public DoubleJumpEvent(Player player, JumpPlayer jumpPlayer) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() { //Required for work
        return handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public JumpPlayer getJumpPlayer() {
        return this.jumpPlayer;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}
