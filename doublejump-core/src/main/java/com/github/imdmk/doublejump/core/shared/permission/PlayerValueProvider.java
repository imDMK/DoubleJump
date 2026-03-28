package com.github.imdmk.doublejump.core.shared.permission;

import org.bukkit.entity.Player;

public interface PlayerValueProvider<T> {
    T resolve(Player player);
}
