package com.github.imdmk.doublejump.injector;

import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.configuration.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.restriction.FlyingRestrictionService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.github.imdmk.doublejump.task.TaskScheduler;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.panda_lang.utilities.inject.annotations.Inject;

public abstract class DefaultInjectable {

    @Inject protected Plugin plugin;

    @Inject protected Server server;

    @Inject protected PluginConfiguration pluginConfiguration;

    @Inject protected JumpConfiguration jumpConfiguration;

    @Inject protected MessageService messageService;

    @Inject protected JumpPlayerCache jumpCache;

    @Inject protected PlayerFlyingService flyingService;

    @Inject protected FlyingRestrictionService restrictionService;

    @Inject protected TaskScheduler taskScheduler;

}
