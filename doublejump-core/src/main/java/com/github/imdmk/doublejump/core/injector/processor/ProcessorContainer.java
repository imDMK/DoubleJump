package com.github.imdmk.doublejump.core.injector.processor;

import java.lang.annotation.Annotation;

public record ProcessorContainer<A extends Annotation>(
        Class<A> annotationType,
        ComponentProcessor<A> processor
) {}
