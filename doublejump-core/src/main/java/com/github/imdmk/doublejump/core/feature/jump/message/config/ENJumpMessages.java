package com.github.imdmk.doublejump.core.feature.jump.message.config;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public final class ENJumpMessages
        extends OkaeriConfig
        implements JumpMessages {

    @Comment({"#", "# Sent when double jump is enabled for the player.", "#"})
    Notice enabled = Notice.chat("<dark_gray>• <green>DoubleJump <white>enabled<dark_gray>.");

    @Comment({"#", "# Sent when double jump is disabled for the player.", "#"})
    Notice disabled = Notice.chat("<dark_gray>• <red>DoubleJump <white>disabled<dark_gray>.");

    @Comment({
            "#",
            "# Sent when double jump is enabled for another player.",
            "# Placeholders:",
            "# {PLAYER} - target player name",
            "#"
    })
    Notice enabledForTarget = Notice.chat("<dark_gray>• <green>Enabled DoubleJump for <white>{PLAYER}<dark_gray>.");

    @Comment({
            "#",
            "# Sent when double jump is disabled for another player.",
            "# Placeholders:",
            "# {PLAYER} - target player name",
            "#"
    })
    Notice disabledForTarget = Notice.chat("<dark_gray>• <red>Disabled DoubleJump for <white>{PLAYER}<dark_gray>.");

    @Comment({"#", "# Sent when cooldown ends and player can jump again.", "#"})
    Notice available = Notice.actionbar("<dark_gray>• <green>You can DoubleJump again");
    
    @Comment({
            "#",
            "# Sent when player tries to jump during cooldown.",
            "# Placeholders:",
            "# {COOLDOWN} - remaining cooldown time",
            "#"
    })
    Notice cooldown = Notice.actionbar("<dark_gray>• <red>Wait <white>{COOLDOWN} <red>before next jump");

    @Comment({"#", "# Sent when double jump is blocked by game mode.", "#"})
    Notice blockedByGameMode = Notice.chat("<dark_gray>• <red>DoubleJump is disabled in this game mode");

    @Comment({"#", "# Sent when player tries to jump while gliding.", "#"})
    Notice blockedByGliding = Notice.actionbar("<dark_gray>• <red>You cannot DoubleJump while gliding");

    @Comment({"#", "# Sent when player is in combat.", "#"})
    Notice blockedByCombat = Notice.chat("<dark_gray>• <red>You are in combat!");

    @Comment({"#", "# Sent when player ping/connection is too high.", "#"})
    Notice blockedByLag = Notice.chat("<dark_gray>• <red>Your connection is too unstable");

    @Comment({"#", "# Sent when double jump is blocked in a region.", "#"})
    Notice blockedByRegion = Notice.chat("<dark_gray>• <red>DoubleJump is disabled in this region");

    @Comment({"#", "# Sent when double jump is blocked in a world.", "#"})
    Notice blockedByWorld = Notice.chat("<dark_gray>• <red>DoubleJump is disabled in this world");

    @Comment({"#", "# Sent when double jump is blocked in a fluid.", "#"})
    Notice blockedByFluid = Notice.actionbar("<dark_gray>• <red>DoubleJump is blocked in water or lava");

    @Comment({"#", "# Sent when double jump is blocked in vehicle", "#"})
    Notice blockedByVehicle = Notice.actionbar("<dark_gray>• <red>DoubleJump is blocked in vehicles");

    @Comment({"#", "# Sent when a player receives a double jump item.", "#"})
    Notice itemGive = Notice.chat(
            "<dark_gray>• <green>You received a <white>DoubleJump item<dark_gray>."
    );

    @Comment({
            "#",
            "# Sent when a player gives a double jump item to another player.",
            "# Placeholders:",
            "# {PLAYER} - target player name",
            "#"
    })
    Notice itemGiveToTarget = Notice.chat(
            "<dark_gray>• <green>Gave <white>DoubleJump item <green>to <white>{PLAYER}<dark_gray>."
    );

    @Comment({"#", "# Sent when a player's double jump item is removed.", "#"})
    Notice itemRemove = Notice.chat(
            "<dark_gray>• <red>Your <white>DoubleJump item <red>was removed<dark_gray>."
    );

    @Comment({
            "#",
            "# Sent when removing a double jump item from another player.",
            "# Placeholders:",
            "# {PLAYER} - target player name",
            "#"
    })
    Notice itemRemoveFromTarget = Notice.chat(
            "<dark_gray>• <red>Removed <white>DoubleJump item <red>from <white>{PLAYER}<dark_gray>."
    );

    @Override
    public Notice enabled() {
        return enabled;
    }

    @Override
    public Notice disabled() {
        return disabled;
    }

    @Override
    public Notice enabledForTarget() {
        return enabledForTarget;
    }

    @Override
    public Notice disabledForTarget() {
        return disabledForTarget;
    }

    @Override
    public Notice available() {
        return available;
    }

    @Override
    public Notice cooldown() {
        return cooldown;
    }

    @Override
    public Notice blockedByGameMode() {
        return blockedByGameMode;
    }

    @Override
    public Notice blockedByGliding() {
        return blockedByGliding;
    }

    @Override
    public Notice blockedByCombat() {
        return blockedByCombat;
    }

    @Override
    public Notice blockedByLag() {
        return blockedByLag;
    }

    @Override
    public Notice blockedByRegion() {
        return blockedByRegion;
    }

    @Override
    public Notice blockedByWorld() {
        return blockedByWorld;
    }

    @Override
    public Notice blockedByFluid() {
        return blockedByFluid;
    }

    @Override
    public Notice blockedByVehicle() {
        return blockedByVehicle;
    }

    @Override
    public Notice itemGive() {
        return itemGive;
    }

    @Override
    public Notice itemGiveToTarget() {
        return itemGiveToTarget;
    }

    @Override
    public Notice itemRemove() {
        return itemRemove;
    }

    @Override
    public Notice itemRemoveFromTarget() {
        return itemRemoveFromTarget;
    }
}