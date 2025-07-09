package com.github.imdmk.doublejump.jump.feature.visual;

import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfig;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for retrieving and initializing visual jump settings.
 * Provides access to default or player-specific jump visuals.
 */
public class JumpVisualService {

    @Inject private Logger logger;
    @Inject private JumpVisualRepository visualRepository;
    @Inject private JumpVisualConfig visualConfiguration;

    /**
     * Retrieves visual settings for the given player UUID.
     * If no settings are found, default visuals are saved and returned.
     *
     * @param uuid unique identifier of the player
     * @return a future with the resolved visual settings
     */
    public CompletableFuture<JumpVisual> getOrCreate(@NotNull UUID uuid) {
        return this.visualRepository.findByUUID(uuid)
                .thenCompose(optional -> optional
                        .map(CompletableFuture::completedFuture)
                        .orElseGet(() -> {
                            JumpVisual defaults = this.getDefaultVisuals();
                            return this.visualRepository.save(uuid, defaults).thenApply(v -> defaults)
                                    .exceptionally(throwable -> {
                                        this.logger.log(Level.SEVERE, "Could not save default visual: " + uuid, throwable);
                                        return null;
                                    });
                        })
                );
    }

    /**
     * Returns the default visual settings defined in the global configuration.
     *
     * @return default jump visuals
     */
    public @NotNull JumpVisual getDefaultVisuals() {
        return this.visualConfiguration.defaultVisuals;
    }
}
