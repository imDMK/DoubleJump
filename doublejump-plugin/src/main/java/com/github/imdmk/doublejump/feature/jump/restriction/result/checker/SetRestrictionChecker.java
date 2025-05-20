package com.github.imdmk.doublejump.feature.jump.restriction.result.checker;

import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class SetRestrictionChecker<T> implements RestrictionChecker {

    private final Set<T> whitelist;
    private final Set<T> blacklist;
    private final Function<Player, T> valueExtractor;
    private final RestrictionDenyReason denyReason;

    public SetRestrictionChecker(
            @NotNull Set<T> whitelist,
            @NotNull Set<T> blacklist,
            @NotNull Function<Player, T> valueExtractor,
            @NotNull RestrictionDenyReason denyReason
    ) {
        this.whitelist = Objects.requireNonNull(whitelist, "whitelist cannot be null");
        this.blacklist = Objects.requireNonNull(blacklist, "blacklist cannot be null");
        this.valueExtractor = Objects.requireNonNull(valueExtractor, "valueExtractor cannot be null");
        this.denyReason = Objects.requireNonNull(denyReason, "denyReason cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        T value = this.valueExtractor.apply(player);

        if (!this.whitelist.isEmpty() && !this.whitelist.contains(value)) {
            return RestrictionResult.failed(this.denyReason);
        }

        if (this.blacklist.contains(value)) {
            return RestrictionResult.failed(this.denyReason);
        }

        return RestrictionResult.passed();
    }
}

