package com.github.imdmk.doublejump.jump;

/**
 * Represents the source or method by which the double jump ability has been activated for a player.
 * This is used to differentiate between manual activation (e.g., via command)
 * and conditional activation (e.g., based on an equipped item).
 */
public enum JumpActivationType {

    /**
     * Indicates that the double jump is not currently activated for the player.
     * This is the default state before any activation method has been applied.
     */
    NONE,

    /**
     * Indicates that the double jump was enabled manually, for example via a command
     * or permanent permission, and should persist regardless of player equipment.
     */
    COMMAND,

    /**
     * Indicates that the double jump was activated automatically when the player joined the server.
     * This is typically used when the feature is globally enabled or remembered from a previous session.
     */
    JOIN,

    /**
     * Indicates that the double jump was enabled due to a specific item,
     * such as special boots. This mode should be deactivated if the item is lost or unequipped.
     */
    ITEM,

    /**
     * Indicates that the double jump was enabled due to the player standing on a specific block,
     * such as a configured jump block. This activation is temporary and only applies while the player
     * is in contact with the block.
     */
    BLOCK

}
