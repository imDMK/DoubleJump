package com.github.imdmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationService {

    private final Set<OkaeriConfig> configs = new HashSet<>();

    public <T extends OkaeriConfig> T create(Class<T> config, File file) {
        T configFile = ConfigurationFactory.create(config, file);

        this.configs.add(configFile);
        return configFile;
    }

    public void reload() {
        for (OkaeriConfig config : this.configs) {
            config.load();
        }
    }
}
