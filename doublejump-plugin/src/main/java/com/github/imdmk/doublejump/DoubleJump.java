package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.restriction.FlyingRestrictionService;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.google.common.base.Stopwatch;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

class DoubleJump implements DoubleJumpApi {

    private final Logger logger;
    private final Server server;

    private final ConfigurationManager configurationManager;

    private final JumpPlayerCache jumpPlayerCache;
    private final FlyingRestrictionService flyingRestrictionService;
    private final PlayerFlyingService playerFlyingService;

    DoubleJump(@NotNull Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Stopwatch stopwatch = Stopwatch.createStarted();
        this.logger = plugin.getLogger();
        this.server = plugin.getServer();

        this.configurationManager = new ConfigurationManager(this.logger, plugin.getDataFolder());

        PluginConfiguration pluginConfiguration = this.configurationManager.create(PluginConfiguration.class);

    }

    void disable() {
        DoubleJumpApiProvider.unregister();
    }

    @Override
    public JumpPlayerCache getJumpPlayerCache() {
        return null;
    }
}
