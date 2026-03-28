package com.github.imdmk.doublejump.core.injector;

import java.util.Comparator;
import java.util.List;

final class ComponentSorter {

    void sort(List<Component<?>> components) {
        components.sort(Comparator
                .comparing((Component<?> c) -> c.priority())
                .thenComparingInt(Component::order)
                .thenComparing(c -> c.type().getName())
        );
    }
}