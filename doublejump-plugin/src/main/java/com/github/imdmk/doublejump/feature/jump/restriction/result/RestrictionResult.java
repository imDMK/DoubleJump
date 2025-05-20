package com.github.imdmk.doublejump.feature.jump.restriction.result;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RestrictionResult(boolean success, Optional<RestrictionDenyReason> reason) {

    public boolean failure() {
        return !this.success;
    }

    public static RestrictionResult passed() {
        return new RestrictionResult(true, Optional.empty());
    }

    public static RestrictionResult failed(RestrictionDenyReason reason) {
        return new RestrictionResult(false, Optional.ofNullable(reason));
    }
}

