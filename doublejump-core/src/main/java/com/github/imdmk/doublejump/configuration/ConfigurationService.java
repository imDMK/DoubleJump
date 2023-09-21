package com.github.imdmk.doublejump.configuration;

import com.github.imdmk.doublejump.configuration.serializer.ItemMetaSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ItemStackSerializer;
import com.github.imdmk.doublejump.configuration.transformer.ColorTransformer;
import com.github.imdmk.doublejump.configuration.transformer.ComponentTransformer;
import com.github.imdmk.doublejump.configuration.transformer.EnchantmentTransformer;
import com.github.imdmk.doublejump.jump.particle.JumpParticleSerializer;
import com.github.imdmk.doublejump.jump.sound.JumpSoundSerializer;
import com.github.imdmk.doublejump.notification.NotificationSerializer;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationService {

    private final Set<OkaeriConfig> okaeriConfigs = new HashSet<>();

    public <T extends OkaeriConfig> T create(Class<T> config, File dataFolder) {
        T configFile = ConfigManager.create(config);

        configFile.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons());
        configFile.withSerdesPack(registry -> {
            registry.register(new ColorTransformer());
            registry.register(new ComponentTransformer());
            registry.register(new EnchantmentTransformer());
            registry.register(new ItemStackSerializer());
            registry.register(new ItemMetaSerializer());
            registry.register(new NotificationSerializer());
            registry.register(new JumpParticleSerializer());
            registry.register(new JumpSoundSerializer());
        });

        configFile.withBindFile(dataFolder);
        configFile.withRemoveOrphans(true);
        configFile.saveDefaults();
        configFile.load(true);

        this.okaeriConfigs.add(configFile);

        return configFile;
    }

    public void reload() {
        for (OkaeriConfig config : this.okaeriConfigs) {
            config.load();
        }
    }
}
