package me.dmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationType;
import me.dmk.doublejump.particle.JumpParticle;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Header("#")
@Header("# Configuration for double jump plugin")
@Header("#")
public class PluginConfiguration extends OkaeriConfig {

    @Comment("# Booleans")
    @Comment("# Check if you are using the latest version when enabling the plugin?")
    public boolean checkForUpdate = true;

    public boolean doubleJumpCommandEnabled = true;

    @Comment("# To auto enable jump mode player must have double jump use permission")
    public boolean enableJumpModeOnJoinForPlayers = true;
    public boolean enableJumpModeOnJoinForAdmins = true;

    @Comment("# NOTE: It is not guaranteed that the player will receive fall damage")
    public boolean jumpFallDamageEnabled = true;
    public boolean jumpParticlesEnabled = true;
    public boolean jumpSoundsEnabled = true;
    public boolean jumpDelayEnabled = true;

    @Comment("# Jump streak")
    public boolean jumpStreaksEnabled = true;
    public boolean jumpStreakResetOnGround = false;
    public boolean jumpStreakResetOnDeath = true;

    @Comment("# Permissions")
    public String doubleJumpCommandPermission = "command.doublejump";
    public String doubleJumpEnableForPermission = "command.doublejump.enablefor";
    public String doubleJumpUsePermission = "doublejump.use";

    @Comment("# Jump settings")
    public double jumpMultiple = 0.5;
    public double jumpUp = 0.5;

    public Duration jumpDelay = Duration.ofSeconds(2);

    @Comment({
            "# Jump sound",
            "# Sound types: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
    })
    public Sound jumpSound = Sound.ENTITY_EXPERIENCE_BOTTLE_THROW;
    public float jumpSoundVolume = 0.20F;
    public float jumpSoundPitch = 1;

    @Comment("# Jump particles")
    public int jumpParticlesCount = 10;
    public int jumpParticlesExtra = 0;
    public double jumpParticlesOffsetX = 0;
    public double jumpParticlesOffsetY = 0;
    public double jumpParticlesOffsetZ = 0;

    public List<JumpParticle> jumpParticles = new ArrayList<>(List.of(
            new JumpParticle(Particle.NOTE, "AQUA", 20),
            new JumpParticle(Particle.REDSTONE, "RED", 40)
    ));

    @Comment("# Restrictions")
    public List<String> disabledWorlds = new ArrayList<>();
    public List<GameMode> disabledGameModes = new ArrayList<>(List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    ));

    @Comment({
            "# Notifications",
            "# Types: CHAT, ACTIONBAR, TITLE, SUBTITLE, DISABLED",
    })
    public Notification playerNotFoundNotification = new Notification(NotificationType.CHAT, "<red>No player found with the given name<dark_gray>.");

    @Comment("# {USAGE} - Correct usage of command")
    public Notification invalidUsageNotification = new Notification(NotificationType.CHAT, "<red>Invalid usage: <dark_red>{USAGE}");
    @Comment("# This messages is used when there is more than one option to use a command")
    public Notification invalidUsageFirstNotification = new Notification(NotificationType.CHAT, "<red>Invalid usage:");
    public Notification invalidUsageListNotification = new Notification(NotificationType.CHAT, "<dark_gray>- <red>{USAGE}");

    @Comment("# {PERMISSIONS} - Required permissions")
    public Notification missingPermissionsNotification = new Notification(NotificationType.CHAT, "<red>Missing permissions: <dark_red>{permissions}<dark_gray>.");

    public Notification jumpModeEnabledNotification = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode!");
    public Notification jumpModeDisabledNotification = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode!");

    @Comment("# {PLAYER} - The name of the player for whom the mode was disabled")
    public Notification jumpModeEnabledForNotification = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode for player {PLAYER}<dark_gray>.");
    public Notification jumpModeDisabledForNotification = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode for player {PLAYER}<dark_gray>.");

    public Notification jumpModeDisabledGameModeNotification = new Notification(NotificationType.CHAT, "<red>U cannot use this command on this game mode.");
    public Notification jumpModeDisabledWorldNotification = new Notification(NotificationType.CHAT, "<red>Double jump has been disabled in this world<dark_gray>.");

    public Notification targetHasDisabledGameModeNotification = new Notification(NotificationType.CHAT, "<red>The player has the game mode disabled<dark_gray>.");
    public Notification targetInDisabledWorldNotification = new Notification(NotificationType.CHAT, "<red>The player is in disabled world<dark_gray>.");

    @Comment("# {STREAK} - Shows current jump streak")
    public Notification jumpStreakIncreaseNotification = new Notification(NotificationType.ACTIONBAR, "<green>Ye! Current streak: <dark_green>{streak}");
    public Notification jumpStreakResetNotification = new Notification(NotificationType.ACTIONBAR, "<red>Ops! Your jump streak has been reset.");
}
