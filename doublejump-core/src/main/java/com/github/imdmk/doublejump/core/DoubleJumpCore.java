package com.github.imdmk.doublejump.core;

import com.github.imdmk.doublejump.core.injector.ComponentManager;
import com.github.imdmk.doublejump.core.injector.processor.ComponentProcessors;
import com.github.imdmk.doublejump.core.injector.subscriber.LocalPublisher;
import com.github.imdmk.doublejump.core.injector.subscriber.Publisher;
import com.github.imdmk.doublejump.core.injector.subscriber.event.DoubleJumpInitializeEvent;
import com.github.imdmk.doublejump.core.injector.subscriber.event.DoubleJumpShutdownEvent;
import com.google.common.base.Stopwatch;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;

import java.io.File;
import java.time.Clock;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

final class DoubleJumpCore {

    private static final String BASE_PACKAGE = "com.github.imdmk.doublejump";

    private final Publisher publisher;

    DoubleJumpCore(Plugin plugin) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Logger logger = plugin.getLogger();

        Injector injector = DependencyInjection.createInjector(resources -> {
            resources.on(Plugin.class).assignInstance(plugin);
            resources.on(Server.class).assignInstance(plugin.getServer());
            resources.on(File.class).assignInstance(plugin.getDataFolder());
            resources.on(Logger.class).assignInstance(logger);
            resources.on(BukkitScheduler.class).assignInstance(plugin.getServer().getScheduler());
            resources.on(Clock.class).assignInstance(Clock::systemDefaultZone);
        });

        injector.getResources().on(Injector.class).assignInstance(() -> injector);

        publisher = new LocalPublisher(injector);

        ComponentManager componentManager = new ComponentManager(injector, BASE_PACKAGE)
                .addProcessors(ComponentProcessors.defaults())
                .addPostProcessor((instance, context) -> publisher.subscribe(instance));

        componentManager.scanAll();
        componentManager.processAll();

        publisher.publish(new DoubleJumpInitializeEvent());

        long elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        logger.info("Successfully loaded plugin in " + elapsedMillis + "ms!");
    }

    void disable() {
        publisher.publish(new DoubleJumpShutdownEvent());
    }
}