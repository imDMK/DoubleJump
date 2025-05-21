package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import org.jetbrains.annotations.NotNull;

public interface DoubleJumpApi {

    @NotNull ConfigurationManager getConfigurationManager();

    JumpPlayerCache getJumpPlayerCache();
}
