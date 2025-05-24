package com.github.imdmk.doublejump.feature.jump.item.usage.impl;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsageStrategy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class HoldItemUsageStrategy implements ItemUsageStrategy {

    @Inject private JumpItemService jumpItemService;

    @Override
    public boolean isItemUsed(@NotNull Player player) {
        if (!this.jumpItemService.isEnabled()) {
            return false;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        return this.jumpItemService.isJumpItem(mainHand) || this.jumpItemService.isJumpItem(offHand);
    }

}
