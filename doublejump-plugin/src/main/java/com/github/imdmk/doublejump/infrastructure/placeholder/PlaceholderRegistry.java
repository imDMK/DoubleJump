package com.github.imdmk.doublejump.infrastructure.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Registry for managing PlaceholderAPI expansions.
 * Ensures proper lifecycle management of registered placeholders.
 */
public final class PlaceholderRegistry {

    private final Set<PlaceholderExpansion> registeredExpansions = Collections.synchronizedSet(new HashSet<>());

    private final PlaceholderHook placeholderHook;

    public PlaceholderRegistry(@NotNull PlaceholderHook placeholderHook) {
        this.placeholderHook = Objects.requireNonNull(placeholderHook, "placeholderHook cannot be null");
    }

    /**
     * Registers and tracks a {@link PlaceholderExpansion}.
     *
     * @param expansion the placeholder expansion to register
     */
    public void register(@NotNull PlaceholderExpansion expansion) {
        if (!this.placeholderHook.isAvailable()) {
            return;
        }

        if (this.registeredExpansions.add(expansion)) {
            expansion.register();
        }
    }

    /**
     * Unregisters a given {@link PlaceholderExpansion}.
     *
     * @param expansion the placeholder expansion to unregister
     */
    public void unregister(@NotNull PlaceholderExpansion expansion) {
        if (this.registeredExpansions.remove(expansion)) {
            expansion.unregister();
        }
    }

    /**
     * Unregisters all tracked {@link PlaceholderExpansion}s.
     */
    public void unregisterAll() {
        synchronized (this.registeredExpansions) {
            for (PlaceholderExpansion expansion : this.registeredExpansions) {
                expansion.unregister();
            }

            this.registeredExpansions.clear();
        }
    }

    /**
     * Returns an unmodifiable view of the currently registered expansions.
     *
     * @return an unmodifiable set of registered expansions
     */
    public Set<PlaceholderExpansion> getRegisteredExpansions() {
        return Collections.unmodifiableSet(this.registeredExpansions);
    }
}
