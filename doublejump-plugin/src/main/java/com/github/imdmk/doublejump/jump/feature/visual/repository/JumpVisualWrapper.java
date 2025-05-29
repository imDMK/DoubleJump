package com.github.imdmk.doublejump.jump.feature.visual.repository;

import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticleWrapper;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSoundPersister;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * ORM entity wrapping JumpVisual settings per player UUID.
 * Responsible for persisting particle effects and sound configuration.
 */
@DatabaseTable(tableName = "jump_visuals")
public class JumpVisualWrapper {

    @DatabaseField(id = true, columnName = "uuid", canBeNull = false)
    private UUID uuid;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<JumpParticleWrapper> particles;

    @DatabaseField(columnName = "sound", persisterClass = JumpSoundPersister.class)
    private JumpSound sound;

    /**
     * Required by ORMLite.
     */
    public JumpVisualWrapper() {}

    private JumpVisualWrapper(
            @NotNull UUID uuid,
            @NotNull ForeignCollection<JumpParticleWrapper> particles,
            @Nullable JumpSound sound
    ) {
        this.uuid = uuid;
        this.particles = particles;
        this.sound = sound;
    }

    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns an unmodifiable list of JumpParticle from wrapped JumpParticleWrapper entities.
     */
    public @NotNull List<JumpParticle> getParticles() {
        return this.particles.stream()
                .map(JumpParticleWrapper::toParticle)
                .toList();
    }

    public void setParticles(@NotNull ForeignCollection<JumpParticleWrapper> particles) {
        this.particles = particles;
    }

    /**
     * Adds a single JumpParticleWrapper to the foreign collection.
     */
    public void addParticle(@NotNull JumpParticleWrapper particleWrapper) {
        this.particles.add(particleWrapper);
    }

    public @Nullable JumpSound getSound() {
        return this.sound;
    }

    public void setSound(@Nullable JumpSound sound) {
        this.sound = sound;
    }

    /**
     * Creates a JumpVisualWrapper entity from runtime JumpVisual instance.
     *
     * @param uuid      player UUID
     * @param visual    runtime JumpVisual settings
     * @param wrapperDao DAO to get an empty foreign collection
     * @return new JumpVisualWrapper ready to persist
     * @throws SQLException if a foreign collection cannot be created
     */
    public static @NotNull JumpVisualWrapper from(
            @NotNull UUID uuid,
            @NotNull JumpVisual visual,
            @NotNull Dao<JumpVisualWrapper, UUID> wrapperDao
    ) throws SQLException {
        JumpVisualWrapper wrapper = new JumpVisualWrapper();
        wrapper.setUuid(uuid);
        wrapper.setSound(visual.getJumpSound().orElse(null));
        wrapper.setParticles(wrapperDao.getEmptyForeignCollection("particles"));

        visual.getJumpParticles().stream()
                .map(particle -> new JumpParticleWrapper(particle, wrapper))
                .forEach(wrapper::addParticle);

        return wrapper;
    }

    /**
     * Converts this entity to a runtime JumpVisual object.
     */
    public @NotNull JumpVisual toVisual() {
        return new JumpVisual(this.getParticles(), this.getSound());
    }
}
