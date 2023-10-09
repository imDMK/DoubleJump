package com.github.imdmk.doublejump.configuration;

import com.github.imdmk.doublejump.configuration.representer.CustomRepresenter;
import com.github.imdmk.doublejump.configuration.serializer.ItemMetaSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ItemStackSerializer;
import com.github.imdmk.doublejump.configuration.transformer.ColorTransformer;
import com.github.imdmk.doublejump.configuration.transformer.ComponentTransformer;
import com.github.imdmk.doublejump.configuration.transformer.EnchantmentTransformer;
import com.github.imdmk.doublejump.jump.particle.JumpParticleSerializer;
import com.github.imdmk.doublejump.jump.sound.JumpSoundSerializer;
import com.github.imdmk.doublejump.notification.configuration.NotificationTransformer;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.File;

public class ConfigurationFactory {

    private ConfigurationFactory() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    public static <T extends OkaeriConfig> T create(Class<T> config, File dataFolder) {
        T configFile = ConfigManager.create(config);

        YamlSnakeYamlConfigurer yamlConfigurer = createYamlSnakeYamlConfigurer();

        configFile.withConfigurer(yamlConfigurer, new SerdesCommons());
        configFile.withSerdesPack(registry -> {
            registry.register(new ColorTransformer());
            registry.register(new ComponentTransformer());
            registry.register(new EnchantmentTransformer());
            registry.register(new ItemStackSerializer());
            registry.register(new ItemMetaSerializer());
            registry.register(new NotificationTransformer());
            registry.register(new JumpParticleSerializer());
            registry.register(new JumpSoundSerializer());
        });

        configFile.withBindFile(dataFolder);
        configFile.withRemoveOrphans(true);
        configFile.saveDefaults();
        configFile.load(true);

        return configFile;
    }

    private static YamlSnakeYamlConfigurer createYamlSnakeYamlConfigurer() {
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(loaderOptions);

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
        dumperOptions.setIndent(2);
        dumperOptions.setSplitLines(false);

        Representer representer = new CustomRepresenter(dumperOptions);
        Resolver resolver = new Resolver();

        Yaml yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions, resolver);
        return new YamlSnakeYamlConfigurer(yaml);
    }
}
