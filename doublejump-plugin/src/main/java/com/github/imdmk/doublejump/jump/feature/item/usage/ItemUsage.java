package com.github.imdmk.doublejump.jump.feature.item.usage;

/**
 * Defines the different modes in which a jump item can be considered "used" by the player.
 * These modes determine when double jump functionality is available based on item interaction or possession.
 */
public enum ItemUsage {

    /**
     * The item must be present anywhere in the player's inventory.
     */
    HAVE_ITEM,

    /**
     * The item must be held in the main hand or off-hand.
     */
    HOLD_ITEM,

    /**
     * The player must actively right-click with the item to activate the jump.
     */
    CLICK_ITEM,

    /**
     * The item must be equipped (e.g., worn as armor).
     */
    WEAR_ITEM
}
