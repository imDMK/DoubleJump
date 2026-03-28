package com.github.imdmk.doublejump.core.platform.hook.eternalcombat;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.platform.hook.PluginHookResolver;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@Service(priority = ComponentPriority.LOWEST, order = 2)
public final class CombatService {

    private final CombatChecker combatChecker;

    @Inject
    CombatService(PluginHookResolver resolver) {
        this.combatChecker = resolver.resolve(
                "EternalCombat",
                EternalCombatChecker::new,
                CombatChecker::empty
        );
    }

    public boolean isInCombat(Player player) {
        return combatChecker.isInCombat(player);
    }
}
