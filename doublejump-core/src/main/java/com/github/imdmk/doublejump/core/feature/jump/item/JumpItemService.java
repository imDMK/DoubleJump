package com.github.imdmk.doublejump.core.feature.jump.item;

import com.github.imdmk.doublejump.core.feature.jump.item.config.JumpItemConfig;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.Arrays;

@Service
public final class JumpItemService {

    private final JumpItemConfig config;
    private final JumpItemMatcher matcher;

    @Inject
    JumpItemService(JumpItemConfig config) {
        this.config = config;
        this.matcher = new JumpItemMatcher(config.item);
    }

    public boolean isEnabled() {
        return config.enabled;
    }

    public boolean isJumpItem(ItemStack item) {
        return matcher.matches(item);
    }

    public boolean isClickMode() {
        return config.usage == JumpItemUsage.CLICK_ITEM;
    }

    public JumpItem getItem() {
        return config.item;
    }

    public boolean isUsingItem(Player player) {
        PlayerInventory inventory = player.getInventory();

        return switch (config.usage) {
            case HOLD_ITEM -> isJumpItem(inventory.getItemInMainHand())
                    || isJumpItem(inventory.getItemInOffHand());
            case WEAR_ITEM -> isJumpItem(inventory.getHelmet())
                    || isJumpItem(inventory.getChestplate())
                    || isJumpItem(inventory.getLeggings())
                    || isJumpItem(inventory.getBoots());
            case HAVE_ITEM -> Arrays.stream(inventory.getContents())
                    .anyMatch(this::isJumpItem);

            case CLICK_ITEM -> false; // handled in interact
        };
    }
}
