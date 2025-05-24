package com.github.imdmk.doublejump.jump;

/**
 * Represents the source or method by which the double jump ability has been activated for a player.
 * This is used to differentiate between manual activation (e.g., via command)
 * and conditional activation (e.g., based on an equipped item).
 */
public enum JumpActivationType {

    NONE,

    /**
     * Indicates that the double jump was enabled manually, for example via a command
     * or permanent permission, and should persist regardless of player equipment.
     */
    MANUAL,

    /**
     * Indicates that the double jump was enabled due to a specific item,
     * such as special boots. This mode should be deactivated if the item is lost or unequipped.
     */
    ITEM
}


