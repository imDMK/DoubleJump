package com.github.imdmk.doublejump.feature.jump.item.usage.impl;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsageStrategy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class HaveItemUsageStrategy implements ItemUsageStrategy {

    @Inject private JumpItemService jumpItemService;

    @Override
    public boolean isItemUsed(@NotNull Player player) {
        if (!this.jumpItemService.isEnabled()) {
            return false;
        }

        return player.getInventory().contains(this.jumpItemService.getJumpItem());
    }
}
