package com.github.imdmk.doublejump.jump.feature.visual.particle.gui;

import com.github.imdmk.doublejump.infrastructure.gui.AbstractGui;
import com.github.imdmk.doublejump.infrastructure.gui.ParameterizedGui;
import com.github.imdmk.doublejump.infrastructure.gui.configuration.ConfigGuiItem;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfig;
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

import java.util.Collections;
import java.util.Map;

public class JumpParticleGui extends AbstractGui implements ParameterizedGui<JumpVisual> {

    public static final String GUI_IDENTIFIER = "jumpParticleGui";

    private static final int ROWS = 6;
    private static final Map<Enchantment, Integer> ACTIVE_ENCHANTMENTS = Map.of(Enchantment.LOYALTY, 3);

    @Inject protected JumpVisualConfig visualConfiguration;

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
        if (this.guiConfig.fillBorder) {
            gui.getFiller().fillBorder(this.guiConfig.borderItem.asGuiItem());
        }
    }

    @Override
    public void prepareItems(@NotNull BaseGui gui, @NotNull Player viewer, @NotNull JumpVisual visual) {
        gui.addItem(this.buildDisableItem(viewer, visual));

        for (JumpParticleEntry entry : this.visualConfiguration.particles.supportedParticles) {
            gui.addItem(this.buildParticleItem(viewer, visual, entry));
        }
    }

    private @NotNull GuiItem buildDisableItem(@NotNull Player viewer, @NotNull JumpVisual visual) {
        ConfigGuiItem.Builder builder = ConfigGuiItem.builder().from(this.getConfig().disableItem);

        if (visual.getJumpParticles().isEmpty()) {
            builder.enchantments(ACTIVE_ENCHANTMENTS);
        }

        return builder.build().asGuiItem(event -> {
            if (event.getClick() == ClickType.LEFT) {
                visual.setJumpParticles(Collections.emptyList());
                this.getConfig().disableItemClickSound.play(viewer);

                this.open(viewer, visual);
            }
        });
    }

    private @NotNull GuiItem buildParticleItem(@NotNull Player viewer, @NotNull JumpVisual visual, @NotNull JumpParticleEntry entry) {
        ItemBuilder builder = ItemBuilder.from(entry.displayItem())
                .name(ComponentUtil.notItalic(this.getConfig().nameColor + entry.particle().particleName()))
                .lore(this.getConfig().defaultLore);

        if (visual.getJumpParticles().contains(entry.particle())) {
            builder.enchant(ACTIVE_ENCHANTMENTS);
            builder.lore(this.getConfig().activeLore);
            builder.flags(ItemFlag.HIDE_ENCHANTS);
        }

        return builder.asGuiItem(event -> this.handleParticleClick(event.getClick(), viewer, visual, entry));
    }

    private void handleParticleClick(@NotNull ClickType click, @NotNull Player viewer, @NotNull JumpVisual visual, @NotNull JumpParticleEntry entry) {
        if (click != ClickType.LEFT) {
            return;
        }

        boolean active = visual.getJumpParticles().contains(entry.particle());

        if (active) {
            visual.removeJumpParticle(entry.particle());
            this.getConfig().disableItemClickSound.play(viewer);
        }
        else {
            visual.addJumpParticle(entry.particle());
            this.getConfig().enableParticleSound.play(viewer);
        }

        this.open(viewer, visual);
    }

    @Override
    public @NotNull String getIdentifier() {
        return GUI_IDENTIFIER;
    }

    private @NotNull JumpParticleGuiConfiguration getConfig() {
        return this.guiConfig.jumpVisualGui.particleGui;
    }
}

