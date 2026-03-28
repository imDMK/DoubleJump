package com.github.imdmk.doublejump.core.platform.hook.worldguard;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.platform.hook.PluginHookResolver;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.Set;

@Service(priority = ComponentPriority.LOWEST, order = 2)
public final class RegionService {

    private final RegionProvider regionProvider;

    @Inject
    RegionService(PluginHookResolver resolver) {
        regionProvider = resolver.resolve(
                "WorldGuard",
                WorldGuardRegionProvider::new,
                RegionProvider::empty
        );
    }

    public Set<String> getRegions(Player player) {
        return regionProvider.queryPlayerRegions(player);
    }
}
