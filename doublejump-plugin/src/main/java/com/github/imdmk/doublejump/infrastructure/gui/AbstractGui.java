package com.github.imdmk.doublejump.infrastructure.gui;

import com.github.imdmk.doublejump.infrastructure.gui.configuration.GuiConfiguration;
import com.github.imdmk.doublejump.infrastructure.injector.DefaultInjectable;
import com.github.imdmk.doublejump.task.TaskScheduler;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.function.Consumer;

public abstract class AbstractGui extends DefaultInjectable {

    @Inject protected GuiConfiguration guiConfiguration;
    @Inject protected TaskScheduler taskScheduler;
    @Inject protected GuiManager guiManager;

    protected void setNextPageItem(@NotNull BaseGui gui) {
        gui.setItem(this.getNextPageItemSlot(gui.getRows()), this.createNextPageItem(gui));
    }

    protected int getNextPageItemSlot(int rows) {
        return switch (rows) {
            case 3 -> 25;
            case 4 -> 34;
            case 5 -> 43;
            case 6 -> 52;
            default -> throw new IllegalStateException("Unexpected row size: " + rows);
        };
    }

    protected GuiItem createNextPageItem(@NotNull BaseGui gui) {
        if (!(gui instanceof PaginatedGui paginatedGui)) {
            throw new IllegalArgumentException("Gui is not a paginated gui to create a next page item");
        }

        return this.guiConfiguration.nextItem.asGuiItem(event -> {
            if (!paginatedGui.next()) {
                paginatedGui.updateItem(event.getSlot(), this.guiConfiguration.noNextItem.asGuiItem());
                this.restoreItemLater(event, gui, this.createNextPageItem(gui));
            }
        });
    }

    protected void setPreviousPageItem(@NotNull BaseGui gui) {
        gui.setItem(this.getPreviousPageItemSlot(gui.getRows()), this.createPreviousPageItem(gui));
    }

    protected int getPreviousPageItemSlot(int rows) {
        return switch (rows) {
            case 3 -> 19;
            case 4 -> 28;
            case 5 -> 37;
            case 6 -> 46;
            default -> throw new IllegalStateException("Unexpected row size: " + rows);
        };
    }

    protected GuiItem createPreviousPageItem(@NotNull BaseGui gui) {
        if (!(gui instanceof PaginatedGui paginatedGui)) {
            throw new IllegalArgumentException("Gui is not a paginated gui to create previous page item");
        }

        return this.guiConfiguration.previousItem.asGuiItem(event -> {
            if (!paginatedGui.previous()) {
                paginatedGui.updateItem(event.getSlot(), this.guiConfiguration.noPreviousItem.asGuiItem());
                this.restoreItemLater(event, gui, this.createPreviousPageItem(gui));
            }
        });
    }

    protected void setExitPageItem(@NotNull BaseGui gui, @NotNull Consumer<InventoryClickEvent> exit) {
        gui.setItem(this.getExitPageItemSlot(gui.getRows()), this.createExitPageItem(exit));
    }

    protected int getExitPageItemSlot(int rows) {
        return switch (rows) {
            case 3 -> 22;
            case 4 -> 31;
            case 5 -> 40;
            case 6 -> 49;
            default -> throw new IllegalStateException("Unexpected row size: " + rows);
        };
    }

    protected GuiItem createExitPageItem(@NotNull Consumer<InventoryClickEvent> exit) {
        return this.guiConfiguration.exitItem.asGuiItem(exit::accept);
    }

    protected void restoreItemLater(@NotNull InventoryClickEvent event, @NotNull BaseGui gui, @NotNull GuiItem item) {
        this.taskScheduler.runLaterAsync(() -> gui.updateItem(event.getSlot(), item), 60L);
    }

}

