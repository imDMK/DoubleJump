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

import java.util.ArrayList;
import java.util.List;

@Header("#")
@Header("# Configuration for double jump plugin")
@Header("#")
public class PluginConfiguration extends OkaeriConfig {

    @Comment("# Commands")
    @Comment("# Enable or disable doublejump command.")
    public boolean doubleJumpCommandEnabled = true;

    @Comment("# Permissions")
    public String doubleJumpCommandPermission = "command.doublejump";
    public String doubleJumpUsePermission = "doublejump.use";

    @Comment("# Notifications (NotificationTypes: CHAT, ACTIONBAR, TITLE, SUBTITLE, To disable chosen notification leave message empty)")
    @Comment("# {PERMISSIONS} - List of required permissions to execute command")
    public Notification missingPermissionNotification = new Notification(
            NotificationType.CHAT, "<red>Missing permission: <dark_red>{permissions}<dark_gray>."
    );

    public Notification jumpModeEnabledNotification = new Notification(
            NotificationType.CHAT, "<green>Enabled double jump mode!"
    );
    public Notification jumpModeDisabledNotification = new Notification(
            NotificationType.CHAT, "<red>Disabled double jump mode!"
    );

    @Comment("# {TIME} - Remaining time to next jump")
    public Notification jumpModeDelayNotification = new Notification(
            NotificationType.CHAT, "<red>Wait {time} before you again jump!"
    );

    public Notification jumpModeDisabledGameModeNotification = new Notification(
            NotificationType.CHAT, "<red>U cannot use this command on this game mode."
    );

    public Notification jumpModeDisabledWorldNotification = new Notification(
            NotificationType.CHAT, "<red>Double jump has been disabled in this world<dark_gray>."
    );

    @Comment("# {STREAK} - Shows current jump streak")
    public Notification jumpStreakIncreaseNotification = new Notification(
            NotificationType.ACTIONBAR, "<green>Ye! Current streak: <dark_green>{streak}"
    );

    public Notification jumpStreakResetNotification = new Notification(
            NotificationType.ACTIONBAR, "<red>Ops! Your jump streak has been reset."
    );

    @Comment("# Jump settings")
    public double jumpMultiple = 0.5;
    public double jumpUp = 0.5;

    @Comment("# Auto enable double jump mode when player join to server (player must have double jump use permission)")
    public boolean jumpEnableOnJoin = true;
    public boolean jumpEnableOnJoinForAdmins = true;

    @Comment("# Reset jump series when player touch ground")
    public boolean jumpStreakResetOnGround = false;
    @Comment("# Reset jump series when player death")
    public boolean jumpStreakResetOnDeath = true;

    @Comment("# Jump delay (in ms, 1 second = 1000ms, Negative value to disable)")
    public int jumpDelay = 2000;

    @Comment("# Name of minecraft sound when player jumps (null value to disable)")
    public Sound jumpSound = Sound.ENTITY_EXPERIENCE_BOTTLE_THROW;

    @Comment("# The volume of the sound")
    public float jumpSoundVolume = 0.20F;
    @Comment("# The pitch of the sound")
    public float jumpSoundPitch = 1;

    @Comment("# List of particles which spawn when player use double jump (supported particles with dust options and without data, leave empty to disable)")
    public List<JumpParticle> jumpParticles = List.of(
            new JumpParticle(Particle.NOTE, "AQUA", 20),
            new JumpParticle(Particle.REDSTONE, "RED", 40)
    );

    @Comment("# Particle data")
    @Comment("# The number of particles")
    public int jumpParticlesCount = 10;
    @Comment("# The maximum random offset of the X/Y/Z axis")
    public double jumpParticlesOffsetX = 0;
    public double jumpParticlesOffsetY = 0;
    public double jumpParticlesOffsetZ = 0;
    @Comment("# The extra data")
    public int jumpParticlesExtra = 0;

    @Comment("# List name of worlds were double jumps will not work")
    public List<String> disabledWorlds = new ArrayList<>();

    @Comment("# List of game modes that cannot use double jump")
    public List<GameMode> disabledGameModes = List.of(
            GameMode.SPECTATOR
    );

    public boolean isDoubleJumpCommandEnabled() {
        return this.doubleJumpCommandEnabled;
    }

    public String getDoubleJumpCommandPermission() {
        return this.doubleJumpCommandPermission;
    }

    public String getDoubleJumpUsePermission() {
        return this.doubleJumpUsePermission;
    }

    public Notification getmissingPermissionNotification() {
        return this.missingPermissionNotification;
    }

    public Notification getjumpModeEnabledNotification() {
        return this.jumpModeEnabledNotification;
    }

    public Notification getjumpModeDisabledNotification() {
        return this.jumpModeDisabledNotification;
    }

    public Notification getjumpModeDelayNotification() {
        return this.jumpModeDelayNotification;
    }

    public Notification getjumpModeDisabledGameModeNotification() {
        return this.jumpModeDisabledGameModeNotification;
    }

    public Notification getjumpModeDisabledWorldNotification() {
        return this.jumpModeDisabledWorldNotification;
    }

    public Notification getJumpStreakIncreaseMessage() {
        return this.jumpStreakIncreaseNotification;
    }

    public Notification getJumpStreakResetMessage() {
        return this.jumpStreakResetNotification;
    }

    public double getJumpMultiple() {
        return this.jumpMultiple;
    }

    public double getJumpUp() {
        return this.jumpUp;
    }

    public boolean isJumpEnableOnJoin() {
        return this.jumpEnableOnJoin;
    }

    public boolean isJumpEnableOnJoinForAdmins() {
        return this.jumpEnableOnJoinForAdmins;
    }

    public boolean isJumpStreakResetOnGround() {
        return this.jumpStreakResetOnGround;
    }

    public boolean isJumpStreakResetOnDeath() {
        return this.jumpStreakResetOnDeath;
    }

    public int getJumpDelay() {
        return this.jumpDelay;
    }

    public Sound getJumpSound() {
        return this.jumpSound;
    }

    public float getJumpSoundVolume() {
        return this.jumpSoundVolume;
    }

    public float getJumpSoundPitch() {
        return this.jumpSoundPitch;
    }

    public List<JumpParticle> getJumpParticles() {
        return this.jumpParticles;
    }

    public int getJumpParticlesCount() {
        return this.jumpParticlesCount;
    }

    public double getJumpParticlesOffsetX() {
        return this.jumpParticlesOffsetX;
    }

    public double getJumpParticlesOffsetY() {
        return this.jumpParticlesOffsetY;
    }

    public double getJumpParticlesOffsetZ() {
        return this.jumpParticlesOffsetZ;
    }

    public int getJumpParticlesExtra() {
        return this.jumpParticlesExtra;
    }

    public List<String> getDisabledWorlds() {
        return this.disabledWorlds;
    }

    public List<GameMode> getDisabledGameModes() {
        return this.disabledGameModes;
    }
}
