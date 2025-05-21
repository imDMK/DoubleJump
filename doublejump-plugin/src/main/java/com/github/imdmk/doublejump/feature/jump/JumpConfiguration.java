package com.github.imdmk.doublejump.feature.jump;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Set;

public class JumpConfiguration extends ConfigSection {

    @Comment("Enables double jump automatically for players when they join the server.")
    public boolean autoEnableOnJoin = true;

    @Comment("# Permission required to auto-enable double jump on join.")
    public String autoEnablePermission = "doublejump.join";

    @Comment("# Auto-enables double jump for players with OP or high permission level.")
    public boolean autoEnableForAdmins = false;

    @Comment("# Determines whether players will receive fall damage after double jumping.")
    public boolean applyFallDamage = true;

    @Comment("# Horizontal boost multiplier applied during a double jump.")
    public double horizontalBoost = 0.6;

    @Comment("# Vertical boost height applied during a double jump.")
    public double verticalBoost = 0.9;

    @Comment("# Jump delay that player must wait before next double jump. Set to 0s to disable this feature.")
    public Duration jumpDelay = Duration.ofSeconds(3L);

    @Comment("# Restrictions that limit or conditionally disable double jumping.")
    public JumpRestrictionConfiguration restrictions = new JumpRestrictionConfiguration();

    public static class JumpRestrictionConfiguration extends OkaeriConfig {

        @Comment("# List of worlds where double jumping is disabled. Leave empty if disable this feature.")
        public Set<String> disabledWorlds = Set.of("world1");

        @Comment("# List of game modes where double jumping is disabled. Leave empty if disable this feature.")
        public Set<GameMode> disabledGameModes = Set.of(GameMode.CREATIVE, GameMode.ADVENTURE);

        @Comment("# List of permissions that allow double jumping. Players must have at least one. Leave empty if disable this feature.")
        public Set<String> allowedPermissions = Set.of();

        @Comment("# Disable double jumping if the player is lagging (e.g., high ping). We recommended this value set to true to avoid player flying.")
        public boolean disableIfPlayerLagging = true;

    }

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesCommons());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "jumpConfiguration.yml";
    }
}

