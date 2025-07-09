package com.github.imdmk.doublejump.jump.feature.visual.gui;

import com.github.imdmk.doublejump.infrastructure.gui.AbstractGui;
import com.github.imdmk.doublejump.infrastructure.gui.ParameterizedGui;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.particle.gui.JumpParticleGui;
import com.github.imdmk.doublejump.jump.feature.visual.sound.gui.JumpSoundGui;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JumpVisualGui extends AbstractGui implements ParameterizedGui<JumpVisual> {

    public static final String GUI_IDENTIFIER = "jumpVisualGui";

    private static final int ROWS = 3;

    @Override
    public @NotNull BaseGui createGui(@NotNull Player viewer, @NotNull JumpVisual parameter) {
        return Gui.gui()
                .title(this.getConfig().title)
                .rows(ROWS)
                .disableAllInteractions()
                .create();
    }

    @Override
    public void prepareBorderItems(@NotNull BaseGui gui) {
        if (this.guiConfig.fillBorder) {
            gui.getFiller().fillBorder(this.guiConfig.borderItem.asGuiItem());
        }
    }

    @Override
    public void prepareNavigationItems(@NotNull BaseGui gui, @NotNull Player viewer, @NotNull JumpVisual visual) {
        this.setExitPageItem(gui, e -> gui.close(viewer));
    }

    @Override
    public void prepareItems(@NotNull BaseGui gui, @NotNull Player viewer, @NotNull JumpVisual visual) {
        gui.setItem(this.getConfig().particleItem.slot(), this.createParticlePageItem(viewer, visual));
        gui.setItem(this.getConfig().soundItem.slot(), this.createSoundPageItem(viewer, visual));
    }

    // Particle page
    private @NotNull GuiItem createParticlePageItem(@NotNull Player viewer, @NotNull JumpVisual visual) {
        return this.getConfig().particleItem
                .asGuiItem(event -> this.openParticlePage(viewer, visual));
    }

    private void openParticlePage(@NotNull Player viewer, @NotNull JumpVisual visual) {
        this.guiManager.openGui(JumpParticleGui.GUI_IDENTIFIER, viewer, visual);
    }

    // Sound page
    private @NotNull GuiItem createSoundPageItem(@NotNull Player viewer, @NotNull JumpVisual visual) {
        return this.getConfig().soundItem
                .asGuiItem(event -> this.openSoundPage(viewer, visual));
    }

    private void openSoundPage(@NotNull Player viewer, @NotNull JumpVisual visual) {
        this.guiManager.openGui(JumpSoundGui.GUI_IDENTIFIER, viewer, visual);
    }

    @Override
    public @NotNull String getIdentifier() {
        return GUI_IDENTIFIER;
    }

    private @NotNull JumpVisualGuiConfiguration getConfig() {
        return this.guiConfig.jumpVisualGui;
    }
}
