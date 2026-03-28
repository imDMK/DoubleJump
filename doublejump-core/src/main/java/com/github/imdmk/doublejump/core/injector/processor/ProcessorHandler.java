package com.github.imdmk.doublejump.core.injector.processor;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface ProcessorHandler<A extends Annotation> {

    void handle(Object instance, A annotation, ComponentProcessorContext context);

}

