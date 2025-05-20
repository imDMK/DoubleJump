package com.github.imdmk.doublejump.jump;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a player performs a double jump.
 * This event is cancellable to allow cancelling the double jump action.
 */
public final class DoubleJumpEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final @NotNull Player player;
    private final @NotNull JumpPlayer jumpPlayer;

    private boolean cancelled;

    /**
     * Constructs a new DoubleJumpEvent.
     *
     * @param player     The player who performed the double jump.
     * @param jumpPlayer The JumpPlayer data associated with the player.
     */
    public DoubleJumpEvent(@NotNull Player player, @NotNull JumpPlayer jumpPlayer) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Returns the static handler list required by Bukkit.
     *
     * @return HandlerList instance for this event.
     */
    public static @NotNull HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Gets the player who performed the double jump.
     *
     * @return The player.
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the JumpPlayer data related to this double jump.
     *
     * @return The JumpPlayer instance.
     */
    public @NotNull JumpPlayer getJumpPlayer() {
        return this.jumpPlayer;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}