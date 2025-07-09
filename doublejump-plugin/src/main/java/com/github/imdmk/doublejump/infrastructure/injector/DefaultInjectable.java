package com.github.imdmk.doublejump.infrastructure.injector;

import com.github.imdmk.doublejump.event.EventCaller;
import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.logging.Logger;

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

    @Inject protected Logger logger;

    @Inject protected JumpConfig jumpConfig;

    @Inject protected MessageService messageService;

    @Inject protected JumpPlayerCache jumpCache;

    @Inject protected PlayerFlyingService flyingService;

    @Inject protected EventCaller eventCaller;

}
