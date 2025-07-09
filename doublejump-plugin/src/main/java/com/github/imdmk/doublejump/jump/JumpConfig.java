package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.config.ConfigSection;
import com.github.imdmk.doublejump.config.MissingConfigValueException;
import com.github.imdmk.doublejump.config.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.config.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.jump.feature.block.JumpBlockConfiguration;
import com.github.imdmk.doublejump.jump.feature.block.JumpBlockSerializer;
import com.github.imdmk.doublejump.jump.feature.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.feature.item.configuration.JumpItemSerializer;
import com.github.imdmk.doublejump.jump.feature.placeholder.JumpPlaceholderConfig;
import com.github.imdmk.doublejump.jump.feature.restriction.JumpRestrictionConfig;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocity;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocitySerializer;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;

@Header({
        "# ",
        "# DoubleJump Premium - Jump Configuration",
        "# ",
        "# This configuration file manages all settings related to the double jump feature.",
        "# It controls how and when double jump is enabled, velocity applied, cooldowns,",
        "# item and block interactions, as well as any restrictions that affect the ability.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class JumpConfig extends ConfigSection {

    @Comment("# Automatically enables double jump for players when they join the server.")
    public boolean autoEnableOnJoin = true;

    @Comment("# Automatically enables double jump for players with permission.")
    public String autoEnableForPermission = "doublejump.join";

    @Comment("# Determines whether players receive fall damage after performing a double jump.")
    public boolean applyFallDamage = true;

    @Comment({
            "# Mapping of permissions or keys to their respective double jump velocity settings.",
            "# Use the 'default' key as a fallback if no specific permission matches.",
            "# Velocity settings can apply based on context such as player join or command."
    })
    public Map<String, JumpVelocity> velocities = Map.of(
            "default", new JumpVelocity(0.3, 0.6),
            "doublejump.join.vip", new JumpVelocity(0.6, 0.9),
            "doublejump.join.supervip", new JumpVelocity(0.9, 1.2)
    );

    @Comment("# Cooldown duration that a player must wait before performing another double jump. Set to 0 to disable.")
    public Duration cooldown = Duration.ofSeconds(3L);

    @Comment("# Configuration for restrictions that limit or disable double jumping under certain conditions.")
    public JumpRestrictionConfig restrictions = new JumpRestrictionConfig();

    @Comment("# Configuration for the item that activates the double jump ability.")
    public JumpItemConfiguration item = new JumpItemConfiguration();

    @Comment("# Configuration for special blocks that interact with the double jump feature.")
    public JumpBlockConfiguration blocks = new JumpBlockConfiguration();

    @Comment("# Configuration for jump placeholders that can be used in-game.")
    public JumpPlaceholderConfig placeholders = new JumpPlaceholderConfig();

    @Override
    public void loadProcessedProperties() {
        if (!this.velocities.containsKey("default")) {
            throw new MissingConfigValueException("velocities.default", "Default jump velocity must be defined for players without specific permissions.");
        }
    }

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesCommons());
            registry.register(new ComponentSerializer());

            registry.register(new JumpVelocitySerializer());

            registry.register(new JumpItemSerializer());
            registry.register(new EnchantmentSerializer());

            registry.register(new JumpBlockSerializer());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "jumpConfig.yml";
    }
}
