package com.github.imdmk.doublejump.feature.jump;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import com.github.imdmk.doublejump.configuration.serializer.ColorSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.feature.jump.block.JumpBlockConfiguration;
import com.github.imdmk.doublejump.feature.jump.block.JumpBlockSerializer;
import com.github.imdmk.doublejump.feature.jump.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.feature.jump.item.configuration.JumpItemSerializer;
import com.github.imdmk.doublejump.feature.jump.particle.JumpParticleConfiguration;
import com.github.imdmk.doublejump.feature.jump.particle.JumpParticleSerializer;
import com.github.imdmk.doublejump.feature.jump.properties.JumpPropertiesSerializer;
import com.github.imdmk.doublejump.feature.jump.restriction.JumpRestrictionConfiguration;
import com.github.imdmk.doublejump.feature.jump.sound.configuration.JumpSoundConfiguration;
import com.github.imdmk.doublejump.feature.jump.sound.configuration.JumpSoundSerializer;
import com.github.imdmk.doublejump.feature.jump.sound.configuration.SoundSerializer;
import com.github.imdmk.doublejump.jump.JumpVelocity;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;

public class JumpConfiguration extends ConfigSection {

    @Comment("Enables double jump automatically for players when they join the server.")
    public boolean autoEnableOnJoin = true;

    @Comment("# Auto-enables double jump for players with OP or high permission level.")
    public boolean autoEnableForAdmins = false;

    @Comment("# Determines whether players will receive fall damage after double jumping.")
    public boolean applyFallDamage = true;

    @Comment({
            "# Map of permissions or keys to their respective double jump velocity settings.",
            "# Use 'default' key for fallback values if no permission matches.",
            "# Players may get these velocity settings based on context: join, command, etc."
    })
    public Map<String, JumpVelocity> jumpVelocity = Map.of(
            "default", new JumpVelocity(0.3, 0.6),
            "doublejump.join.vip", new JumpVelocity(0.6, 0.9),
            "doublejump.join.supervip", new JumpVelocity(0.9, 1.2)
    );

    @Comment("# Jump delay that player must wait before next double jump. Set to 0s to disable this feature.")
    public Duration jumpDelay = Duration.ofSeconds(3L);

    @Comment("# Restrictions that limit or conditionally disable double jumping.")
    public JumpRestrictionConfiguration restrictions = new JumpRestrictionConfiguration();

    @Comment("# Configuration for visual particle effects triggered by double jump.")
    public JumpParticleConfiguration particles = new JumpParticleConfiguration();

    @Comment("# Configuration for sounds triggered by double jump.")
    public JumpSoundConfiguration sounds = new JumpSoundConfiguration();

    @Comment("# Configuration for the item that activates the double jump ability.")
    public JumpItemConfiguration jumpItem = new JumpItemConfiguration();

    @Comment("# Configuration for special blocks related to double jump feature.")
    public JumpBlockConfiguration jumpBlock = new JumpBlockConfiguration();

    @Override
    public void loadProcessedProperties() {
        if (!this.jumpVelocity.containsKey("default")) {
            throw new IllegalArgumentException("The field 'jumpProperties' must contain a 'default' key!");
        }
    }

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesCommons());
            registry.register(new ComponentSerializer());

            registry.register(new JumpPropertiesSerializer());

            registry.register(new ColorSerializer());
            registry.register(new JumpParticleSerializer());

            registry.register(new JumpSoundSerializer());
            registry.register(new SoundSerializer());

            registry.register(new JumpItemSerializer());
            registry.register(new EnchantmentSerializer());

            registry.register(new JumpBlockSerializer());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "jumpConfiguration.yml";
    }
}
