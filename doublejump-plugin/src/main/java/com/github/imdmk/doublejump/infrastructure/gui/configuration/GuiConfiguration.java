package com.github.imdmk.doublejump.infrastructure.gui.configuration;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import com.github.imdmk.doublejump.configuration.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.jump.feature.visual.gui.JumpVisualGuiConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.sound.configuration.JumpSoundSerializer;
import com.github.imdmk.doublejump.jump.feature.visual.sound.configuration.SoundSerializer;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Header({
        "# ",
        "# DoubleJump Premium - Gui Configuration",
        "# Configure GUI items and appearance for the plugin.",
        "# ",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class GuiConfiguration extends ConfigSection {

    @Comment("# Enable border around all GUIs")
    public boolean fillBorder = true;

    @Comment("# Item used as the border around GUIs")
    public ConfigGuiItem borderItem = ConfigGuiItem.builder()
            .material(Material.GRAY_STAINED_GLASS_PANE)
            .name(" ")
            .lore(" ")
            .build();

    @Comment("# Item used to navigate to the next page")
    public ConfigGuiItem nextItem = ConfigGuiItem.builder()
            .material(Material.ARROW)
            .name("<green>Next page")
            .lore(" ", "<gray>Click <red>RIGHT <gray>to go to the next page", " ")
            .build();

    @Comment("# Item shown when there is no next page")
    public ConfigGuiItem noNextItem = ConfigGuiItem.builder()
            .material(Material.BARRIER)
            .name("<red>There's no next page!")
            .lore(" ", "<red>Sorry, there is no next page available.", " ")
            .build();

    @Comment("# Item used to navigate to the previous page")
    public ConfigGuiItem previousItem = ConfigGuiItem.builder()
            .material(Material.ARROW)
            .name("<green>Previous page")
            .lore(" ", "<gray>Click <red>LEFT <gray>to go to the previous page", " ")
            .build();

    @Comment("# Item shown when there is no previous page")
    public ConfigGuiItem noPreviousItem = ConfigGuiItem.builder()
            .material(Material.BARRIER)
            .name("<red>There's no previous page!")
            .lore(" ", "<red>Sorry, there is no previous page available.", " ")
            .build();

    @Comment("# Item used to exit the GUI")
    public ConfigGuiItem exitItem = ConfigGuiItem.builder()
            .material(Material.ACACIA_BUTTON)
            .name("<red>Exit GUI")
            .lore(" ", "<gray>Click <red>LEFT <gray>to exit this GUI", " ")
            .build();

    @Comment("# Configuration for the jump visual GUI")
    public JumpVisualGuiConfiguration jumpVisualGui = new JumpVisualGuiConfiguration();

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new ComponentSerializer());
            registry.register(new EnchantmentSerializer());

            registry.register(new SoundSerializer());
            registry.register(new JumpSoundSerializer());
            registry.register(new ConfigGuiItemSerializer());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "guiConfiguration.yml";
    }
}
