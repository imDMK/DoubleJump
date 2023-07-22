package me.dmk.doublejump.player;

import java.time.Duration;
import java.time.Instant;

/**
 * Player class used for double jumps
 */
public class JumpPlayer {

    private Instant endOfDelay;
    private int streak;

    public JumpPlayer() {
        this.endOfDelay = Instant.MIN;
        this.streak = 0;
    }

    public JumpPlayer(Instant delay, int streak) {
        this.endOfDelay = delay;
        this.streak = streak;
    }

    public Instant getEndOfDelay() {
        return this.endOfDelay;
    }

    public Duration getRemainingDelayDuration() {
        return Duration.between(Instant.now(), this.endOfDelay);
    }

    public boolean canUseJump() {
        return Instant.now().isAfter(this.endOfDelay);
    }

    /**
     * Adds a delay to the current time
     * @param durationToAdd duration to add
     */
    public void addDelay(Duration durationToAdd) {
        this.endOfDelay = Instant.now().plus(durationToAdd);
    }

    public void setEndOfDelay(Instant endOfDelay) {
        this.endOfDelay = endOfDelay;
    }

    public int getStreak() {
        return this.streak;
    }

    /**
     * Increases streak by 1
     * @return The new jump streak
     */
    public int increaseStreak() {
        return this.streak += 1;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }
}
