package com.github.imdmk.doublejump.feature.jump.item.usage.impl;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsageStrategy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class WearItemUsageStrategy implements ItemUsageStrategy {

    @Inject private JumpItemService jumpItemService;

    @Override
    public boolean isItemUsed(@NotNull Player player) {
        if (!this.jumpItemService.isEnabled()) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();

        return this.jumpItemService.isJumpItem(inventory.getHelmet())
                || this.jumpItemService.isJumpItem(inventory.getChestplate())
                || this.jumpItemService.isJumpItem(inventory.getLeggings())
                || this.jumpItemService.isJumpItem(inventory.getBoots());
    }
}
