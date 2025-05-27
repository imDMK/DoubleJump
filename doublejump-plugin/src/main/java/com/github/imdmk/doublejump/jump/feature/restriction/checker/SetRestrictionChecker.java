package com.github.imdmk.doublejump.jump.feature.restriction.checker;

import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class SetRestrictionChecker<T> implements RestrictionChecker {

    private final Set<T> disabled;
    private final Function<Player, T> valueExtractor;
    private final RestrictionDenyReason denyReason;

    public SetRestrictionChecker(
            @NotNull Set<T> disabled,
            @NotNull Function<Player, T> valueExtractor,
            @NotNull RestrictionDenyReason denyReason
    ) {
        this.disabled = Objects.requireNonNull(disabled, "required cannot be null");
        this.valueExtractor = Objects.requireNonNull(valueExtractor, "valueExtractor cannot be null");
        this.denyReason = Objects.requireNonNull(denyReason, "denyReason cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        T value = this.valueExtractor.apply(player);

        if (!this.disabled.isEmpty() && this.disabled.contains(value)) {
            return RestrictionResult.failed(this.denyReason);
        }

        return RestrictionResult.passed();
    }
}
