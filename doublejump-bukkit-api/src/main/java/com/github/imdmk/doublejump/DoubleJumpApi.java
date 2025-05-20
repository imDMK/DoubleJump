package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.github.imdmk.doublejump.task.TaskScheduler;

public interface DoubleJumpApi {

    JumpPlayerCache getJumpPlayerCache();

    ConfigurationManager getConfigurationManager();

    TaskScheduler getTaskScheduler();
}
