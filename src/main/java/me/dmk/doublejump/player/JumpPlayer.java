package me.dmk.doublejump.player;

import java.time.Instant;

/**
 * Player class used for double jumps
 */
public class JumpPlayer {

    private Instant jumpDelay;
    private int jumpStreak;

    public JumpPlayer() {
        this.jumpDelay = null;
        this.jumpStreak = 0;
    }

    public Instant getDelay() {
        return this.jumpDelay;
    }

    public void setJumpDelay(Instant jumpDelay) {
        this.jumpDelay = jumpDelay;
    }

    /**
     * Sets a delay from the current time
     * @param delay time to add in milliseconds
     */
    public void addDelay(int delay) {
        this.jumpDelay = Instant.now().plusMillis(delay);
    }

    public boolean canUseJump() {
        if (this.jumpDelay == null) {
            return true;
        }

        return Instant.now().isAfter(this.jumpDelay);
    }

    public int getJumpStreak() {
        return this.jumpStreak;
    }

    public void setJumpStreak(int jumpStreak) {
        this.jumpStreak = jumpStreak;
    }

    /**
     * Increases jump streak by 1
     */
    public void increaseJumpStreak() {
        this.jumpStreak += 1;
    }

    /**
     * Setting jump streak to 0
     */
    public void resetJumpStreak() {
        this.jumpStreak = 0;
    }
}
