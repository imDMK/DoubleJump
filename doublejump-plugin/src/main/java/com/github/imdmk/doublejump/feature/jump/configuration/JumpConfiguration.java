package com.github.imdmk.doublejump.feature.jump.configuration;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class JumpConfiguration extends ConfigSection {

    public boolean enableDoubleJumpOnJoin = true;

    public String enableDoubleJumpOnJoinForPermission = "doublejump.join";

    public boolean enableDoubleJumpOnJoinForAdmins = false;

    public boolean fallDamage = true;

    public double doubleJumpMultiplier = 0.3;

    public double doubleJumpUp = 0.6;

    public JumpRestrictionConfiguration restrictions = new JumpRestrictionConfiguration();

    public static class JumpRestrictionConfiguration extends OkaeriConfig {

        public Set<String> worldWhitelist = Set.of();
        public Set<String> worldBlacklist = Set.of("world1");

        public Set<GameMode> gameModeWhitelist = Set.of();
        public Set<GameMode> gameModeBlacklist = Set.of(GameMode.CREATIVE, GameMode.ADVENTURE);

        public Set<String> permissionWhitelist = Set.of();
        public Set<String> permissionBlacklist = Set.of("unallowed-permission");
    }

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {};
    }

    @Override
    public @NotNull String getFileName() {
        return "jumpConfiguration.yml";
    }
}
