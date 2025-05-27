package com.github.imdmk.doublejump.jump.feature.visual.sound.gui;

import com.github.imdmk.doublejump.infrastructure.gui.AbstractGui;
import com.github.imdmk.doublejump.infrastructure.gui.ParameterizedGui;
import com.github.imdmk.doublejump.infrastructure.gui.configuration.item.ConfigGuiItem;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.gui.JumpVisualGui;
import com.github.imdmk.doublejump.util.ComponentUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.Map;

public class JumpSoundGui extends AbstractGui implements ParameterizedGui<JumpVisual> {

    public static final String GUI_IDENTIFIER = "jumpSoundGui";

    private static final int ROWS = 6;
    private static final Map<Enchantment, Integer> ACTIVE_ENCHANTMENTS = Map.of(Enchantment.LOYALTY, 3);

    @Inject protected JumpVisualConfiguration visualConfiguration;

    @Override
    public @NotNull BaseGui createGui(@NotNull Player viewer, @NotNull JumpVisual visual) {
        return Gui.paginated()
                .title(this.getConfig().title)
                .rows(ROWS)
                .disableAllInteractions()
                .create();
    }

    @Override
    public void prepareNavigationItems(@NotNull BaseGui gui, @NotNull Player viewer, @NotNull JumpVisual visual) {
        this.setNextPageItem(gui);
        this.setPreviousPageItem(gui);
        this.setExitPageItem(gui, e -> this.guiManager.openGui(JumpVisualGui.GUI_IDENTIFIER, viewer, visual));
    }

    @Override
    public void prepareBorderItems(@NotNull BaseGui gui) {
        if (this.guiConfiguration.fillBorder) {
            gui.getFiller().fillBorder(this.guiConfiguration.borderItem.asGuiItem());
        }
    }

    @Override
    public void prepareItems(@NotNull BaseGui gui, @NotNull Player viewer, @NotNull JumpVisual visual) {
        gui.addItem(this.buildDisableItem(viewer, visual));

        for (JumpSoundEntry entry : this.visualConfiguration.sounds.supportedSounds) {
            gui.addItem(this.buildSoundItem(viewer, visual, entry));
        }
    }

    private @NotNull GuiItem buildDisableItem(@NotNull Player viewer, @NotNull JumpVisual visual) {
        ConfigGuiItem.Builder builder = ConfigGuiItem.builder().from(this.getConfig().disableItem);

        if (visual.getJumpSound().isEmpty()) {
            builder.enchantments(ACTIVE_ENCHANTMENTS);
        }

        return builder.build().asGuiItem(event -> {
            if (event.getClick() != ClickType.RIGHT) {
                visual.setJumpSound(null);
                this.getConfig().disableItemClickSound.play(viewer);

                this.open(viewer, visual);
            }
        });
    }

    private @NotNull GuiItem buildSoundItem(@NotNull Player viewer, @NotNull JumpVisual visual, @NotNull JumpSoundEntry entry) {
        ItemBuilder builder = ItemBuilder.from(entry.displayItem())
                .name(ComponentUtil.notItalic(this.getConfig().nameColor + entry.jumpSound().getName()))
                .lore(this.getConfig().defaultLore);

        if (visual.isJumpSound(entry.jumpSound())) {
            builder.lore(this.getConfig().activeLore);
            builder.enchant(ACTIVE_ENCHANTMENTS);
            builder.flags(ItemFlag.HIDE_ENCHANTS);
        }

        return builder.asGuiItem(event -> this.handleSoundClick(event.getClick(), viewer, visual, entry));
    }

    private void handleSoundClick(@NotNull ClickType click, @NotNull Player viewer, @NotNull JumpVisual visual, @NotNull JumpSoundEntry entry) {
        entry.jumpSound().play(viewer);

        if (click == ClickType.RIGHT) {
            visual.setJumpSound(entry.jumpSound());
            this.open(viewer, visual);
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return GUI_IDENTIFIER;
    }

    private @NotNull JumpSoundGuiConfiguration getConfig() {
        return this.guiConfiguration.jumpVisualGui.soundGui;
    }
}
