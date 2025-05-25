package com.github.imdmk.doublejump.jump.event;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpVelocity;
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

    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull Player player;
    private final @NotNull JumpPlayer jumpPlayer;
    private @NotNull JumpVelocity jumpVelocity;

    private boolean cancelled;

    /**
     * Constructs a new DoubleJumpEvent.
     *
     * @param player         The player who performed the double jump.
     * @param jumpPlayer     The JumpPlayer data associated with the player.
     * @param jumpVelocity The jump properties (boosts) applied during this double jump.
     */
    public DoubleJumpEvent(
            final @NotNull Player player,
            final @NotNull JumpPlayer jumpPlayer,
            final @NotNull JumpVelocity jumpVelocity) {
        this.player = player;
        this.jumpPlayer = jumpPlayer;
        this.jumpVelocity = jumpVelocity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Returns the static handler list required by Bukkit.
     *
     * @return HandlerList instance for this event.
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
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

    /**
     * Gets the jump properties (horizontal and vertical boosts) applied during this double jump.
     *
     * @return The DoubleJumpProperties instance.
     */
    public @NotNull JumpVelocity getJumpProperties() {
        return this.jumpVelocity;
    }

    /**
     * Sets the jump properties to be applied during this double jump.
     *
     * @param jumpVelocity The new jump properties.
     */
    public void setJumpProperties(final @NotNull JumpVelocity jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
