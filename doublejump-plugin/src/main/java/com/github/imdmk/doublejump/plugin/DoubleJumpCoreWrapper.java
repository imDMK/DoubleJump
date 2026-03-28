package com.github.imdmk.doublejump.plugin;

import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class DoubleJumpCoreWrapper {

    private static final String LOADER_CORE_CLASS = "com.github.imdmk.doublejump.core.DoubleJumpCore";

    private final Class<?> coreClass;
    private Object core;

    DoubleJumpCoreWrapper(Class<?> coreClass) {
        this.coreClass = coreClass;
    }

    void enable(Plugin plugin) {
        try {
            Constructor<?> coreConstructor = this.coreClass.getDeclaredConstructor(Plugin.class);
            coreConstructor.setAccessible(true);

            core = coreConstructor.newInstance(plugin);
        } catch (InvocationTargetException exception) {
            if (exception.getCause() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException("Can not enable DoubleJump: ", exception.getCause());
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException exception) {
            throw new RuntimeException(exception);
        }
    }

    void disable() {
        try {
            Method disableMethod = this.coreClass.getDeclaredMethod("disable");

            disableMethod.setAccessible(true);

            if (core != null) {
                disableMethod.invoke(core);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    static DoubleJumpCoreWrapper create(ClassLoader loader) {
        try {
            Class<?> coreClass = Class.forName(LOADER_CORE_CLASS, true, loader);
            return new DoubleJumpCoreWrapper(coreClass);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

}