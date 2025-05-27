package com.github.imdmk.doublejump.jump.feature.visual.repository.impl;

import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EmptyJumpVisualRepository implements JumpVisualRepository {

    @Override
    public CompletableFuture<Optional<JumpVisual>> findByUUID(@NotNull UUID uuid) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException());
    }

    @Override
    public CompletableFuture<JumpVisual> save(@NotNull UUID uuid, @NotNull JumpVisual visual) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException());
    }

    @Override
    public CompletableFuture<Void> delete(@NotNull UUID uuid) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException());
    }
}
