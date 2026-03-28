package com.github.imdmk.doublejump.core.feature.jump.message.config;

import com.eternalcode.multification.notice.Notice;

public interface JumpMessages {

    Notice enabled();
    Notice disabled();

    Notice enabledForTarget();
    Notice disabledForTarget();

    Notice available();

    Notice cooldown();

    Notice blockedByGameMode();
    Notice blockedByGliding();
    Notice blockedByCombat();
    Notice blockedByLag();
    Notice blockedByRegion();
    Notice blockedByWorld();
    Notice blockedByFluid();
    Notice blockedByVehicle();

    Notice itemGive();
    Notice itemGiveToTarget();
    Notice itemRemove();
    Notice itemRemoveFromTarget();
}