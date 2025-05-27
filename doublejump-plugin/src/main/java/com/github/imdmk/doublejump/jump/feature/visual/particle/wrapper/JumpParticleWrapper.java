package com.github.imdmk.doublejump.jump.feature.visual.particle.wrapper;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualWrapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * ORM wrapper for JumpParticle used in database persistence.
 */
@DatabaseTable(tableName = "jump_particles")
public class JumpParticleWrapper {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "visual_id")
    private JumpVisualWrapper visual;

    @DatabaseField(columnName = "particle", canBeNull = false)
    private String particle;

    @DatabaseField(columnName = "count", canBeNull = false)
    private int count;

    // Required by ORMLite
    public JumpParticleWrapper() {}

    public JumpParticleWrapper(@NotNull JumpParticle particle, @NotNull JumpVisualWrapper visual) {
        this.particle = particle.particle().name();
        this.count = particle.count();
        this.visual = visual;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull JumpVisualWrapper visual() {
        return this.visual;
    }

    public @NotNull JumpParticle toParticle() {
        return new JumpParticle(Particle.valueOf(this.particle), this.count);
    }

    public @NotNull String particleName() {
        return this.particle;
    }

    public int count() {
        return this.count;
    }
}


