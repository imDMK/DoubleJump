package com.github.imdmk.doublejump.injector;

import org.bukkit.event.Listener;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

public abstract class PluginListener extends DefaultInjectable implements Listener {

    @PostConstruct
    public void postConstruct() {
        this.server.getPluginManager().registerEvents(this, this.plugin);
    }

}
