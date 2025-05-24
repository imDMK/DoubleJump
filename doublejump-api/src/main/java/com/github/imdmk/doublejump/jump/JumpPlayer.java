package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.jump.restriction.RestrictionDenyReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the double jump state of a player.
 * Stores runtime flags and timestamps used to control jump availability and restrictions.
 */
public class JumpPlayer {

    private final UUID uuid;
    private final String name;
    /**
     * Defines how the double jump ability was activated for the player.
     * This can be used to differentiate between manual activation (e.g. via command)
     * and item-based activation (e.g. special boots).
     */
    private JumpActivationType activationType = JumpActivationType.NONE;

    /**
     * Indicates whether double jump is currently active for this player (toggled on).
     * Does not necessarily mean the player is allowed to jump (see {@link #jumpAllowed}).
     */
    private boolean active = false;
    /**
     * Indicates whether the player is currently allowed to perform a double jump.
     * This is affected by runtime restrictions such as cooldowns or conditions.
     * Defaults to {@code true}.
     */
    private boolean jumpAllowed = true;

    /**
     * Timestamp of the player's last performed jump.
     * Used for applying cooldowns and delay-based restrictions.
     */
    private Instant lastJump;
    /**
     * The last restriction reason that was notified to the player.
     * Used to prevent sending duplicate restriction messages.
     */
    private RestrictionDenyReason lastNotifiedReason;

    /**
     * Creates a new JumpPlayer instance for the given player UUID and name.
     *
     * @param uuid the unique identifier of the player
     * @param name the player name
     */
    public JumpPlayer(@NotNull UUID uuid, @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Gets the UUID of the player.
     *
     * @return the player's UUID
     */
    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the name of the player.
     *
     * @return the player's name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Gets the current jump activation type for the player.
     * This indicates how the double jump was initially enabled (e.g. by command or item).
     *
     * @return the activation type of the double jump
     */
    public JumpActivationType getActivationType() {
        return this.activationType;
    }

    /**
     * Checks whether the player's current activation type matches the given type.
     * This can be used to determine how the double jump was enabled (e.g. via item or manually).
     *
     * @param type the activation type to compare against, must not be null
     * @return true if the activation type matches and is not null, false otherwise
     */
    public boolean isActivationType(@NotNull JumpActivationType type) {
        return this.activationType.equals(type);
    }

    /**
     * Sets the activation type for the double jump.
     * Should be updated if the source of jump permission changes during gameplay.
     *
     * @param activationType the new activation type
     */
    public void setActivationType(@Nullable JumpActivationType activationType) {
        this.activationType = activationType;
    }

    /**
     * Checks whether the jump feature is currently active for the player.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets whether the jump feature is active for the player.
     *
     * @param active true to activate, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks whether the player is currently allowed to perform a jump.
     *
     * @return true if jumping is allowed, false otherwise
     */
    public boolean isJumpAllowed() {
        return this.jumpAllowed;
    }

    /**
     * Sets whether the player is allowed to jump.
     *
     * @param jumpAllowed true to allow jumping, false to prevent it
     */
    public void setJumpAllowed(boolean jumpAllowed) {
        this.jumpAllowed = jumpAllowed;
    }

    /**
     * Gets the timestamp of the player's last jump, if present.
     *
     * @return an optional containing the last jump time, or empty if not set
     */
    public Optional<Instant> getLastJump() {
        return Optional.ofNullable(this.lastJump);
    }

    /**
     * Sets the timestamp of the player's last jump.
     *
     * @param lastJump the time the last jump occurred
     */
    public void setLastJump(@Nullable Instant lastJump) {
        this.lastJump = lastJump;
    }

    /**
     * Checks if the given restriction reason is the same as the last notified one.
     *
     * @param reason the reason to compare, not null
     * @return true if the reason matches the last-notified reason, false otherwise
     */
    public boolean isSameAsLastNotifiedReason(@NotNull RestrictionDenyReason reason) {
        return Objects.equals(this.lastNotifiedReason, reason);
    }

    /**
     * Checks if there is a last notified restriction reason stored.
     *
     * @return true if a last notified reason is set, false otherwise
     */
    public boolean hasLastNotifiedReason() {
        return this.lastNotifiedReason != null;
    }

    /**
     * Sets the last restriction reason notified to the player.
     *
     * @param lastNotifiedReason the reason to set, may be null
     */
    public void setLastNotifiedReason(@Nullable RestrictionDenyReason lastNotifiedReason) {
        this.lastNotifiedReason = lastNotifiedReason;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JumpPlayer that)) {
            return false;
        }

        return this.getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return this.getUuid().hashCode();
    }

    @Override
    public String toString() {
        return "JumpPlayer{" +
                "uuid=" + this.uuid +
                ", name='" + this.name + '\'' +
                ", active=" + this.active +
                ", jumpAllowed=" + this.jumpAllowed +
                ", lastJump=" + this.lastJump +
                ", lastNotifiedReason=" + this.lastNotifiedReason +
                '}';
    }
}
