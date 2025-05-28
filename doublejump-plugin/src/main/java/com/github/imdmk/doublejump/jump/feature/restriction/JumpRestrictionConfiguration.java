package com.github.imdmk.doublejump.jump.feature.restriction;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.util.Set;

public class JumpRestrictionConfiguration extends OkaeriConfig {

    @Comment("# List of worlds where double jumping is disabled. Leave empty to disable this restriction.")
    public Set<String> disabledWorlds = Set.of("world1");

    @Comment("# List of WorldGuard regions where double jumping is disabled. Leave empty to disable this restriction.")
    public Set<String> disabledRegions = Set.of();

    @Comment("# List of game modes where double jumping is disabled. Leave empty to disable this restriction.")
    public Set<GameMode> disabledGameModes = Set.of(GameMode.CREATIVE, GameMode.ADVENTURE);

    @Comment("# List of permissions that allow double jumping. Players must have at least one. Leave empty to disable this restriction.")
    public Set<String> allowedPermissions = Set.of();

    @Comment("# Disable double jumping if the player is lagging (e.g., high ping). Recommended to set this to true to prevent unintended flying.")
    public boolean disableIfPlayerLagging = true;

    @Comment("# Block double jump usage while the player is gliding (e.g., with elytra)")
    public boolean blockUsageWhileGliding = true;
}
