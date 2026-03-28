package com.github.imdmk.doublejump.core.feature.jump.effect;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.shared.permission.PermissionBasedValueProvider;
import com.github.imdmk.doublejump.core.shared.permission.PlayerValueProvider;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.List;

@Service(priority = ComponentPriority.LOW)
public final class JumpEffectService {

    private final PlayerValueProvider<List<JumpEffect>> effectsProvider;

    @Inject
    JumpEffectService(JumpConfig config) {
        this.effectsProvider = new PermissionBasedValueProvider<>(config.effects);
    }

    public void apply(Player player) {
        for (JumpEffect effect : effectsProvider.resolve(player)) {
            effect.apply(player);
        }
    }
}
