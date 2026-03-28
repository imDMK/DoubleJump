package com.github.imdmk.doublejump.core.platform.hook.eternalcombat;

import com.eternalcode.combat.EternalCombatProvider;
import com.eternalcode.combat.fight.FightManager;
import org.bukkit.entity.Player;

public final class EternalCombatChecker implements CombatChecker {

    private static final FightManager FIGHT_MANAGER = EternalCombatProvider.provide()
            .getFightManager();

    @Override
    public boolean isInCombat(Player player) {
        return FIGHT_MANAGER.isInCombat(player.getUniqueId());
    }
}
