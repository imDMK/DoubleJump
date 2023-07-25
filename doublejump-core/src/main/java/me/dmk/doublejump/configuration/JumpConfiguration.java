package me.dmk.doublejump.configuration;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.dmk.doublejump.particle.JumpParticle;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class JumpConfiguration extends OkaeriConfig {

    @Comment("# To auto enable jump mode player must have double jump use permission")
    public boolean enableJumpModeOnJoinForPlayers = true;
    public boolean enableJumpModeOnJoinForAdmins = true;

    public boolean jumpFallDamageEnabled = true;

    @Comment("# Jump streak settings")
    public boolean jumpStreaksEnabled = true;
    public boolean jumpStreakResetOnGround = false;
    public boolean jumpStreakResetOnDeath = true;

    @Comment("# Jump settings")
    public double jumpMultiple = 0.3;
    public double jumpUp = 0.6;

    @Comment("# Jump delay settings")
    public boolean jumpDelayEnabled = false;

    public Duration jumpDelay = Duration.ofSeconds(2);

    @Comment({
            "# Jump item settings",
            "# Required item to use double jump",
            "# Determines if the player can receive and use double jump boots"
    })
    public boolean jumpItemEnabled = false;

    @Comment({
            "# Decrease the durability of an item after using it",
            "# Set to 0 to disable"
    })
    public int reduceJumpItemDurability = 0;

    @Comment({
            "# Cancel item usage?",
            "# Useful when you want the player to be unable to wear, for example, boots"
    })
    public boolean cancelJumpItemUse = true;
    @Comment("# Use a double jump after using a double jump item?")
    public boolean jumpItemUseDoubleJump = true;

    @Comment("# Remove jump item after player use jump item?")
    public boolean removeJumpItemAfterUse = true;
    @Comment("# Disable double jump mode after player use jump item?")
    public boolean disableJumpModeAfterUse = true;
    @Comment({
            "# Toggle double jump mode after player uses jump item?",
            "# The player can decide if he wants double jump mode"
    })
    public boolean jumpItemUseSwitchDoubleJump = false;

    @Comment("# Disable double jump mode when player drops jump item?")
    public boolean jumpItemDropCancelJumpMode = false;
    @Comment("# Delete jump item when player drops this item?")
    public boolean jumpItemDropDelete = false;
    @Comment("# Cancel drop item when player tries to drop jump item?")
    public boolean jumpItemDropCancel = false;

    @Comment({
            "# Cancel jump item repair in anvil?",
            "# If you allow it, the player will be able to repair the item, but cannot rename it (Renaming will make the item inoperable)."
    })
    public boolean jumpItemCancelRepair = false;

    @Comment({
            "# This specifies the use of a double jump",
            "# Available usages:",
            "# HAVE_ITEM - The player must have the jump item in inventory",
            "# HOLD_ITEM - The player must hold the jump item",
            "# CLICK_ITEM - The player must right click item",
            "# WEAR_ITEM - The player must wear item",
    })
    public JumpItemUsage jumpItemUsage = JumpItemUsage.CLICK_ITEM;

    public ItemStack jumpItem = ItemBuilder.from(Material.DIAMOND_BOOTS)
            .name(Component.text("<red>DOUBLE JUMP"))
            .lore(Component.empty(), Component.text("<red>This is double jump item!"), Component.empty())
            .flags(ItemFlag.HIDE_ATTRIBUTES)
            .enchant(Enchantment.DURABILITY, 10)
            .build();

    @Comment({
            "# Jump sound settings",
            "# Sound types: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
    })
    public boolean jumpSoundsEnabled = true;

    public Sound jumpSound = Sound.ENTITY_EXPERIENCE_BOTTLE_THROW;
    public float jumpSoundVolume = 0.20F;
    public float jumpSoundPitch = 1;

    @Comment("# Jump particles settings")
    public boolean jumpParticlesEnabled = true;

    public int jumpParticlesCount = 3;
    public int jumpParticlesExtra = 2;
    public double jumpParticlesOffsetX = 0;
    public double jumpParticlesOffsetY = 0;
    public double jumpParticlesOffsetZ = 0;

    public List<JumpParticle> jumpParticles = new ArrayList<>(List.of(
            new JumpParticle(Particle.NOTE, "WHITE", 20),
            new JumpParticle(Particle.REDSTONE, "WHITE", 40)
    ));

    @Comment("# Restrictions")
    public List<String> disabledWorlds = new ArrayList<>();
    public List<GameMode> disabledGameModes = new ArrayList<>(List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    ));
}
