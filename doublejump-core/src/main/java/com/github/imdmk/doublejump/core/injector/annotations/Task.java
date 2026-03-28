package com.github.imdmk.doublejump.core.injector.annotations;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Task {

    long delayMillis();

    long periodMillis();

    boolean async() default true;

    ComponentPriority priority() default ComponentPriority.HIGHEST;
}
