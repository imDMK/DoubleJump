package com.github.imdmk.doublejump.jump.feature.item.usage;

import com.github.imdmk.doublejump.jump.feature.item.usage.impl.HaveItemUsageStrategy;
import com.github.imdmk.doublejump.jump.feature.item.usage.impl.HoldItemUsageStrategy;
import com.github.imdmk.doublejump.jump.feature.item.usage.impl.WearItemUsageStrategy;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.DependencyInjectionException;
import org.panda_lang.utilities.inject.Injector;

/**
 * Factory class responsible for creating {@link ItemUsageStrategy} instances
 * based on the provided {@link ItemUsage} type.
 * <p>
 * The strategies are instantiated using the given {@link Injector}, which
 * allows dependency injection into the strategy classes.
 */
public final class ItemUsageStrategyFactory {

    private ItemUsageStrategyFactory() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    /**
     * Creates an {@link ItemUsageStrategy} for the specified usage mode using the provided injector.
     *
     * @param usage the desired usage mode
     * @param injector the injector to instantiate the strategy
     * @return an instance of the corresponding {@link ItemUsageStrategy}
     * @throws DependencyInjectionException if instantiation fails
     */
    public static ItemUsageStrategy create(@NotNull ItemUsage usage, @NotNull Injector injector) throws DependencyInjectionException {
        return switch (usage) {
            case HAVE_ITEM -> injector.newInstanceWithFields(HaveItemUsageStrategy.class);
            case HOLD_ITEM -> injector.newInstanceWithFields(HoldItemUsageStrategy.class);
            case WEAR_ITEM -> injector.newInstanceWithFields(WearItemUsageStrategy.class);
            default -> player -> false;
        };
    }
}

