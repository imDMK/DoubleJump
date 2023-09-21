package com.github.imdmk.doublejump.jump;

import java.time.Duration;
import java.time.Instant;

/**
 * Player class used for double jumps
 */
public class JumpPlayer {

    private Instant endOfDelay;
    private int streak;

    private int jumps;
    private final int jumpsLimit;

    private Instant endOfJumpsRegenerationDelay;

    private boolean delayNotificationReceived;
    private boolean jumpsNotificationReceived;

    public JumpPlayer() {
        this.endOfDelay = Instant.MIN;
        this.streak = 0;
        this.jumps = -1;
        this.jumpsLimit = -1;
        this.endOfJumpsRegenerationDelay = Instant.MIN;
    }

    public JumpPlayer(int jumps, int jumpsLimit) {
        this.endOfDelay = Instant.MIN;
        this.streak = 0;
        this.jumps = jumps;
        this.jumpsLimit = jumpsLimit;
        this.endOfJumpsRegenerationDelay = Instant.MIN;
    }

    /**
     * Checks if the delay has passed
     * @return true if now is ahead of the delay
     */
    public boolean isDelay() {
        return Instant.now().isBefore(this.endOfDelay);
    }

    /**
     * @return The duration from now until the end of the delay
     */
    public Duration getRemainingDelayDuration() {
        return Duration.between(Instant.now(), this.endOfDelay);
    }

    public Instant getEndOfDelay() {
        return this.endOfDelay;
    }

    /**
     * Adds a delay to the current time
     * @param toAdd duration to add
     */
    public void addDelay(Duration toAdd) {
        this.endOfDelay = Instant.now().plus(toAdd);
    }

    public void setEndOfDelay(Instant endOfDelay) {
        this.endOfDelay = endOfDelay;
    }

    public int getStreak() {
        return this.streak;
    }

    /**
     * Adds a streak
     * @param toAdd streak to add
     * @return The new streak
     */
    public int addStreak(int toAdd) {
        return this.streak += toAdd;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * Checks if player has jumps
     * Returns always true if the jumps are -1
     * @return true if player has jumps
     */
    public boolean hasJumps() {
        if (this.jumps == -1) {
            return true;
        }

        return this.jumps > 0;
    }

    /**
     * Adds a jumps
     * @param toAdd jumps to add
     * @return The new jumps
     */
    public int addJumps(int toAdd) {
        return this.jumps += toAdd;
    }

    /**
     * Removes a jumps
     * @param toRemove jumps to remove
     * @return The new jumps
     */
    public int removeJumps(int toRemove) {
        return this.jumps -= toRemove;
    }

    public int getJumps() {
        return this.jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public int getJumpsLimit() {
        return this.jumpsLimit;
    }

    /**
     * Adds a delay to the current time
     * @param toAdd duration to add
     */
    public void addJumpRegenerationDelay(Duration toAdd) {
        this.endOfJumpsRegenerationDelay = Instant.now().plus(toAdd);
    }

    /**
     * @return The duration from now until the end of the delay
     */
    public Duration getRemainingJumpRegenerationDuration() {
        return Duration.between(Instant.now(), this.endOfJumpsRegenerationDelay);
    }

    public Instant getEndOfJumpsRegenerationDelay() {
        return this.endOfJumpsRegenerationDelay;
    }

    public void setEndOfJumpsRegenerationDelay(Instant endOfJumpsRegenerationDelay) {
        this.endOfJumpsRegenerationDelay = endOfJumpsRegenerationDelay;
    }

    public boolean isDelayNotificationReceived() {
        return this.delayNotificationReceived;
    }

    public void setDelayNotificationReceived(boolean delayNotificationReceived) {
        this.delayNotificationReceived = delayNotificationReceived;
    }

    public boolean isJumpsNotificationReceived() {
        return this.jumpsNotificationReceived;
    }

    public void setJumpsNotificationReceived(boolean jumpsNotificationReceived) {
        this.jumpsNotificationReceived = jumpsNotificationReceived;
    }
}
