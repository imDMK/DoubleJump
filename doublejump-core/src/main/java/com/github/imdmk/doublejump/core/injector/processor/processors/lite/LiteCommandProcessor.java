package com.github.imdmk.doublejump.core.injector.processor.processors.lite;

import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteCommand;
import com.github.imdmk.doublejump.core.injector.processor.ComponentProcessor;
import com.github.imdmk.doublejump.core.injector.processor.ComponentProcessorContext;
import com.github.imdmk.doublejump.core.platform.litecommand.LiteCommandsConfigurer;

public final class LiteCommandProcessor implements ComponentProcessor<LiteCommand> {

    private final LiteCommandsConfigurer liteCommandsConfigurer;

    public LiteCommandProcessor(LiteCommandsConfigurer liteCommandsConfigurer) {
        this.liteCommandsConfigurer = liteCommandsConfigurer;
    }

    @Override
    public Class<LiteCommand> annotation() {
        return LiteCommand.class;
    }

    @Override
    public void process(
            Object instance,
            LiteCommand annotation,
            ComponentProcessorContext context
    ) {
        liteCommandsConfigurer.builder().commands(instance);
    }
}
