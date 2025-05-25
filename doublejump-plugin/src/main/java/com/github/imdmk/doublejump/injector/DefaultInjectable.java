package com.github.imdmk.doublejump.injector;

import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.feature.jump.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.properties.JumpVelocityService;
import com.github.imdmk.doublejump.feature.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.task.TaskScheduler;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Base class providing common dependencies for plugin components.
 * <p>
 * All fields are injected automatically and provide access to core plugin services,
 * configurations, and APIs for convenience in subclasses.
 * </p>
 */
public abstract class DefaultInjectable {

    @Inject protected Plugin plugin;

    @Inject protected Server server;

    @Inject protected PluginConfiguration pluginConfiguration;

    @Inject protected JumpConfiguration jumpConfiguration;

    @Inject protected MessageService messageService;

    @Inject protected JumpPlayerCache jumpCache;

    @Inject protected JumpVelocityService jumpVelocityService;

    @Inject protected PlayerFlyingService flyingService;

    @Inject protected JumpRestrictionService restrictionService;

    @Inject protected TaskScheduler taskScheduler;

}
