package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import com.github.imdmk.doublejump.configuration.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.jump.feature.block.JumpBlockConfiguration;
import com.github.imdmk.doublejump.jump.feature.block.JumpBlockSerializer;
import com.github.imdmk.doublejump.jump.feature.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.feature.item.configuration.JumpItemSerializer;
import com.github.imdmk.doublejump.jump.feature.restriction.JumpRestrictionConfiguration;
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
public class JumpConfiguration extends ConfigSection {

    @Comment("# Automatically enables double jump for players when they join the server.")
    public boolean autoEnableOnJoin = true;

    @Comment("# Automatically enables double jump for operators or players with high permissions.")
    public boolean autoEnableForAdmins = false;

    @Comment("# Determines whether players receive fall damage after performing a double jump.")
    public boolean applyFallDamage = true;

    @Comment({
            "# Mapping of permissions or keys to their respective double jump velocity settings.",
            "# Use the 'default' key as a fallback if no specific permission matches.",
            "# Velocity settings can apply based on context such as player join or command."
    })
    public Map<String, JumpVelocity> jumpVelocity = Map.of(
            "default", new JumpVelocity(0.3, 0.6),
            "doublejump.join.vip", new JumpVelocity(0.6, 0.9),
            "doublejump.join.supervip", new JumpVelocity(0.9, 1.2)
    );

    @Comment("# Cooldown duration that a player must wait before performing another double jump. Set to 0 to disable.")
    public Duration jumpDelay = Duration.ofSeconds(3L);

    @Comment("# Configuration for restrictions that limit or disable double jumping under certain conditions.")
    public JumpRestrictionConfiguration jumpRestrictions = new JumpRestrictionConfiguration();

    @Comment("# Configuration for the item that activates the double jump ability.")
    public JumpItemConfiguration jumpItem = new JumpItemConfiguration();

    @Comment("# Configuration for special blocks that interact with the double jump feature.")
    public JumpBlockConfiguration jumpBlock = new JumpBlockConfiguration();

    @Override
    public void loadProcessedProperties() {
        if (!this.jumpVelocity.containsKey("default")) {
            throw new IllegalArgumentException("The 'jumpVelocity' map must contain a 'default' key!");
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
        return "jumpConfiguration.yml";
    }
}
