package com.github.imdmk.doublejump.core.feature.jump;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class JumpEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final JumpPlayer jumpPlayer;

    private boolean cancelled;

    public JumpEvent(
            @NotNull Player player,
            @NotNull JumpPlayer jumpPlayer
    ) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public JumpPlayer getJumpPlayer() {
        return jumpPlayer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
