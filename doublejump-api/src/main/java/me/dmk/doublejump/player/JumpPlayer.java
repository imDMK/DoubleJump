package me.dmk.doublejump.player;

import java.time.Duration;
import java.time.Instant;

/**
 * Player class used for double jumps
 */
public class JumpPlayer {

    private Instant delay;
    private int streak;

    public JumpPlayer(Instant delay, int streak) {
        this.delay = delay;
        this.streak = streak;
    }

    public JumpPlayer() {
        this.delay = Instant.MIN;
        this.streak = 0;
    }

    public Instant getDelay() {
        return this.delay;
    }

    public boolean canUseJump() {
        return Instant.now().isAfter(this.delay);
    }

    /**
     * Sets a delay from the current time
     * @param duration duration to add
     */
    public void addDelay(Duration duration) {
        this.delay = Instant.now().plus(duration);
    }

    public void setDelay(Instant delay) {
        this.delay = delay;
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
