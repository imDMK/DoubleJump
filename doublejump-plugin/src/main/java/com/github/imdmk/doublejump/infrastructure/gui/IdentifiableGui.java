package com.github.imdmk.doublejump.infrastructure.gui;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IdentifiableGui {

    @NotNull String getIdentifier();

}
