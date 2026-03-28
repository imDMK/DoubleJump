package com.github.imdmk.doublejump.core.platform.hook;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.bukkit.Server;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.function.Supplier;
import java.util.logging.Logger;

@Service(priority = ComponentPriority.LOWEST)
public final class PluginHookResolver {

    private final Logger logger;
    private final Server server;

    @Inject
    PluginHookResolver(Logger logger, Server server) {
        this.logger = logger;
        this.server = server;
    }

    public <T> T resolve(String pluginName, Supplier<T> supplier, Supplier<T> fallback) {
        if (server.getPluginManager().isPluginEnabled(pluginName)) {
            logger.info("Hooked into " + pluginName + " plugin.");
            return supplier.get();
        }

        logger.info(pluginName + " not found; skipping");
        return fallback.get();
    }
}
