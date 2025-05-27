package com.github.imdmk.doublejump.jump.feature.visual.repository.impl;

import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.particle.wrapper.JumpParticleWrapper;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualWrapper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoJumpVisualRepository implements JumpVisualRepository {

    private final Logger logger;
    private final Dao<JumpVisualWrapper, UUID> visualDao;
    private final ExecutorService executor;

    private final JumpVisualCache visualCache;

    public DaoJumpVisualRepository(
            @NotNull Logger logger,
            @Nullable ConnectionSource connectionSource,
            @NotNull JumpVisualCache visualCache) throws SQLException {
        this.logger = Objects.requireNonNull(logger, "logger cannot be null");
        Objects.requireNonNull(connectionSource, "connectionSource cannot be null");

        this.visualDao = DaoManager.createDao(connectionSource, JumpVisualWrapper.class);
        this.executor = Executors.newCachedThreadPool();
        this.visualCache = Objects.requireNonNull(visualCache, "Visual Cache cannot be null");

        TableUtils.createTableIfNotExists(connectionSource, JumpVisualWrapper.class);
        TableUtils.createTableIfNotExists(connectionSource, JumpParticleWrapper.class);
    }

    @Override
    public CompletableFuture<Optional<JumpVisual>> findByUUID(@NotNull UUID uuid) {
        Optional<JumpVisual> cachedVisual = this.visualCache.getByUuid(uuid);
        if (cachedVisual.isPresent()) {
            return CompletableFuture.completedFuture(cachedVisual);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<JumpVisual> visualOptional = Optional.ofNullable(this.visualDao.queryBuilder()
                                .where().eq("uuid", uuid)
                                .queryForFirst())
                        .map(JumpVisualWrapper::toVisual);

                visualOptional.ifPresent(visual -> this.visualCache.cache(uuid, visual));

                return visualOptional;
            }
            catch (SQLException sqlException) {
                this.logger.log(Level.SEVERE, "An error occurred while retrieving settings for " + uuid, sqlException);
                throw new CompletionException(sqlException);
            }
        }, this.executor).orTimeout(3L, TimeUnit.SECONDS);
    }

    @Override
    public CompletableFuture<JumpVisual> save(@NotNull UUID uuid, @NotNull JumpVisual visual) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JumpVisualWrapper wrapper = JumpVisualWrapper.from(uuid, visual, this.visualDao);
                this.visualDao.createOrUpdate(wrapper);
                this.visualCache.cache(uuid, visual);
                return visual;
            }
            catch (SQLException sqlException) {
                this.logger.log(Level.SEVERE, "An error occurred while saving settings for " + uuid, sqlException);
                throw new CompletionException(sqlException);
            }
        }, this.executor).orTimeout(3L, TimeUnit.SECONDS);
    }

    @Override
    public CompletableFuture<Void> delete(@NotNull UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                this.visualDao.deleteById(uuid);
                this.visualCache.evict(uuid);
            }
            catch (SQLException sqlException) {
                this.logger.log(Level.SEVERE, "An error occurred while deleting settings for " + uuid, sqlException);
                throw new CompletionException(sqlException);
            }
        }, this.executor).orTimeout(3L, TimeUnit.SECONDS);
    }
}
