package me.dmk.doublejump.player;

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
        this.streak = 0;
    }

    public Instant getDelay() {
        return this.delay;
    }

    public void setDelay(Instant delay) {
        this.delay = delay;
    }

    /**
     * Sets a delay from the current time
     * @param millis milliseconds to add
     */
    public void addDelay(long millis) {
        this.delay = Instant.now().plusMillis(millis);
    }

    public int getStreak() {
        return this.streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * Increases streak by 1
     * @return The new jump streak
     */
    public int increaseStreak() {
        return this.streak += 1;
    }

    public boolean canUseJump() {
        if (this.delay == null) {
            return true;
        }

        return Instant.now().isAfter(this.delay);
    }
}
