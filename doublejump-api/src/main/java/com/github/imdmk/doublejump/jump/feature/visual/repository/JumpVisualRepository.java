package com.github.imdmk.doublejump.jump.feature.visual.repository;

import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Repository interface for managing persistence of {@link JumpVisual} settings.
 * <p>
 * Provides asynchronous operations for retrieving, saving, and deleting visual configurations
 * associated with player UUIDs.
 */
public interface JumpVisualRepository {

    /**
     * Asynchronously retrieves visual settings associated with the given player UUID.
     *
     * @param uuid unique player identifier
     * @return a {@link CompletableFuture} that completes with an {@link Optional} containing
     *         the visual settings if present, or empty if not found
     */
    CompletableFuture<Optional<JumpVisual>> findByUUID(@NotNull UUID uuid);

    /**
     * Asynchronously saves the given visual settings for the specified player UUID.
     * <p>
     * May insert or update an existing record depending on the implementation.
     *
     * @param uuid     unique player identifier
     * @param settings visual settings to persist
     * @return a {@link CompletableFuture} that completes with the saved {@link JumpVisual}
     */
    CompletableFuture<JumpVisual> save(@NotNull UUID uuid, @NotNull JumpVisual settings);

    /**
     * Asynchronously deletes the visual settings associated with the given player UUID.
     *
     * @param uuid unique player identifier
     * @return a {@link CompletableFuture} that completes when the deletion is done
     */
    CompletableFuture<Void> delete(@NotNull UUID uuid);
}
