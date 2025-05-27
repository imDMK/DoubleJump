package com.github.imdmk.doublejump.jump.feature.visual.sound.configuration;

import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import com.github.imdmk.doublejump.jump.feature.visual.sound.gui.JumpSoundEntry;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

public class JumpSoundConfiguration extends OkaeriConfig {

    @Comment("# Enables or disables the double jump sounds.")
    public boolean enabled = true;

    @Comment("# List of sounds that players can choose via gui.")
    public List<JumpSoundEntry> supportedSounds = List.of(
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 0F), Material.EXPERIENCE_BOTTLE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F), Material.BLAZE_ROD),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_CAT_PURR, 1.0F, 1.0F), Material.CAT_SPAWN_EGG),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_CHICKEN_AMBIENT, 1.0F, 1.0F), Material.CHICKEN_SPAWN_EGG),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_COW_AMBIENT, 1.0F, 1.0F), Material.COW_SPAWN_EGG),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F), Material.DRAGON_BREATH),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_GHAST_SCREAM, 1.0F, 1.0F), Material.GHAST_TEAR),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_GUARDIAN_AMBIENT, 1.0F, 1.0F), Material.PRISMARINE_SHARD),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_HORSE_AMBIENT, 1.0F, 1.0F), Material.SADDLE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F), Material.IRON_INGOT),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F), Material.GOLDEN_AXE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F), Material.IRON_SWORD),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_SHEEP_AMBIENT, 1.0F, 1.0F), Material.WHITE_WOOL),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_SKELETON_AMBIENT, 1.0F, 1.0F), Material.BONE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_SLIME_JUMP, 1.0F, 1.0F), Material.SLIME_BALL),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_SNOWBALL_THROW, 1.0F, 1.0F), Material.SNOWBALL),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_SPIDER_AMBIENT, 1.0F, 1.0F), Material.SPIDER_EYE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_ZOMBIE_AMBIENT, 1.0F, 1.0F), Material.ROTTEN_FLESH),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F), Material.ANVIL),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F), Material.ENCHANTING_TABLE),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F), Material.GLASS),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_LAVA_EXTINGUISH, 1.0F, 1.0F), Material.LAVA_BUCKET),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F), Material.BELL),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_PORTAL_TRIGGER, 1.0F, 1.0F), Material.ENDER_PEARL),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_SAND_BREAK, 1.0F, 1.0F), Material.SAND),
            new JumpSoundEntry(new JumpSound(Sound.BLOCK_WOOD_BREAK, 1.0F, 1.0F), Material.OAK_PLANKS),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F), Material.EXPERIENCE_BOTTLE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 1.0F), Material.FIREWORK_ROCKET),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F), Material.APPLE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_PLAYER_BREATH, 1.0F, 1.0F), Material.POTION),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F), Material.EXPERIENCE_BOTTLE),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F), Material.TNT),
            new JumpSoundEntry(new JumpSound(Sound.ENTITY_GENERIC_HURT, 1.0F, 1.0F), Material.IRON_SWORD),
            new JumpSoundEntry(new JumpSound(Sound.UI_BUTTON_CLICK, 1.0F, 1.0F), Material.STONE_BUTTON),
            new JumpSoundEntry(new JumpSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F), Material.CHEST)
    );
}
