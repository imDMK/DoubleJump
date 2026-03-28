package com.github.imdmk.doublejump.core.injector.processor;

import com.github.imdmk.doublejump.core.injector.annotations.ConfigFile;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.injector.annotations.Task;
import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteArgument;
import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteCommand;
import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteContext;
import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteHandler;
import com.github.imdmk.doublejump.core.injector.processor.processors.ConfigFileProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.PluginListenerProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.ServiceProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.TaskProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.lite.LiteArgumentProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.lite.LiteCommandProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.lite.LiteContextProcessor;
import com.github.imdmk.doublejump.core.injector.processor.processors.lite.LiteHandlerProcessor;

import java.util.List;

public final class ComponentProcessors {

    private ComponentProcessors() {
        throw new UnsupportedOperationException("This is utility class and cannot be instantiated.");
    }

    public static List<ProcessorContainer<?>> defaults() {
        return List.of(
                ProcessorBuilder.of(ConfigFile.class, ConfigFileProcessor.class).build(),
                ProcessorBuilder.of(Service.class, ServiceProcessor.class).build(),
                ProcessorBuilder.of(PluginListener.class, PluginListenerProcessor.class).build(),
                ProcessorBuilder.of(Task.class, TaskProcessor.class).build(),
                ProcessorBuilder.of(LiteCommand.class, LiteCommandProcessor.class).build(),
                ProcessorBuilder.of(LiteArgument.class, LiteArgumentProcessor.class).build(),
                ProcessorBuilder.of(LiteContext.class, LiteContextProcessor.class).build(),
                ProcessorBuilder.of(LiteHandler.class, LiteHandlerProcessor.class).build()
        );
    }
}
