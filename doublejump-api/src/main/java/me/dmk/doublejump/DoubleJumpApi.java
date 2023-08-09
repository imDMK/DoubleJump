package me.dmk.doublejump;

import me.dmk.doublejump.jump.JumpPlayerManager;

import javax.annotation.Nonnull;

public interface DoubleJumpApi {

    /**
     * <p>Gets the {@link JumpPlayerManager}</p>
     * A class used to change the player's double jump mode or check if the player has permission to use double jump.
     *
     * @return The jump player manager
     */
    @Nonnull JumpPlayerManager getJumpPlayerManager();
}
