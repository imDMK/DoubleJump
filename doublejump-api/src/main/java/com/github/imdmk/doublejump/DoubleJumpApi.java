package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.region.RegionProvider;

import javax.annotation.Nonnull;

public interface DoubleJumpApi {

    /**
     * <p>Gets the {@link RegionProvider}</p>
     * The class used to check if a player is in a restricted area.
     *
     * @return The region provider
     */
    @Nonnull RegionProvider getRegionProvider();

    /**
     * <p>Gets the {@link JumpPlayerManager}</p>
     * A class used to change the player's double jump mode or check if the player has permission to use double jump.
     *
     * @return The jump player manager
     */
    @Nonnull JumpPlayerManager getJumpPlayerManager();
}
