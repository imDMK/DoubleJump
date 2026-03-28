package com.github.imdmk.doublejump.core.injector.processor;

@FunctionalInterface
public interface ComponentPostProcessor {

    void postProcess(Object instance, ComponentProcessorContext context);

}


