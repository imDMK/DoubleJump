package com.github.imdmk.doublejump.jump.feature.restriction;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.util.Set;

public class JumpRestrictionConfiguration extends OkaeriConfig {

    @Comment("# List of worlds where double jumping is disabled. Leave empty if disable this feature.")
    public Set<String> disabledWorlds = Set.of("world1");

    @Comment("# List of game modes where double jumping is disabled. Leave empty if disable this feature.")
    public Set<GameMode> disabledGameModes = Set.of(GameMode.CREATIVE, GameMode.ADVENTURE);

    @Comment("# List of permissions that allow double jumping. Players must have at least one. Leave empty if disable this feature.")
    public Set<String> allowedPermissions = Set.of();

    @Comment("# Disable double jumping if the player is lagging (e.g., high ping). We recommended this value set to true to avoid player flying.")
    public boolean disableIfPlayerLagging = true;

}
