package com.github.imdmk.doublejump.core.platform.hook.eternalcombat;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface CombatChecker {

    boolean isInCombat(Player player);

    static CombatChecker empty() {
        return player -> false;
    }
}
