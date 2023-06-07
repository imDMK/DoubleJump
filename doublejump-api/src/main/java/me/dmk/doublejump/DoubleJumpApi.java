package me.dmk.doublejump;

import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.player.JumpPlayerMap;

import javax.annotation.Nonnull;

public interface DoubleJumpApi {

    /**
     * Gets the {@link JumpPlayerManager}
     *
     * <p>Class used to change the player's double jump mode with change of flying and flying ability.
     * And you can use it to check if the player has permission to double jump.</p>
     *
     * @return The jump player manager
     */
    @Nonnull JumpPlayerManager getJumpPlayerManager();

    /**
     * Gets the {@link JumpPlayerMap}
     *
     * <p>A class that holds players who have double jump mode enabled.
     * You can use it to disable the player's double jump mode</p>
     *
     * @return The jump player map
     */
    @Nonnull JumpPlayerMap getJumpPlayerMap();
}
