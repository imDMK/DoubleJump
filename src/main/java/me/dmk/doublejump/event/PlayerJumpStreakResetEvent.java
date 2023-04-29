package me.dmk.doublejump.event;

import me.dmk.doublejump.player.JumpPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJumpStreakResetEvent extends Event implements Cancellable {

    private final Player player;
    private final JumpPlayer jumpPlayer;

    private boolean canceled;

    private static final HandlerList handlerList = new HandlerList();

    public PlayerJumpStreakResetEvent(Player player, JumpPlayer jumpPlayer) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() { //Required for work
        return handlerList;
    }
}
