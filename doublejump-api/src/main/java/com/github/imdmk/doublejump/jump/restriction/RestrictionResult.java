package com.github.imdmk.doublejump.jump.restriction;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents the result of checking all flying or jump-related restrictions.
 * <p>
 * A result is considered successful if no restriction has denied the action.
 * If the result is a failure, an optional reason is provided to indicate why it was denied.
 *
 * @param success {@code true} if all restrictions passed, {@code false} otherwise
 * @param reason  Optional reason describing why the restrictions failed, present only if {@code success == false}
 */
public record RestrictionResult(boolean success, @NotNull Optional<RestrictionDenyReason> reason) {

    /**
     * Returns whether the restriction check failed.
     *
     * @return {@code true} if the result is a failure
     */
    public boolean failure() {
        return !this.success;
    }

    /**
     * Returns whether the restriction associated with this result is considered hard,
     * which typically requires immediate disabling of the double jump feature.
     *
     * @return true if the restriction is hard, false otherwise
     */
    public boolean isHardRestriction() {
        return this.reason().map(RestrictionDenyReason::isHardRestriction).orElse(false);
    }

    /**
     * Creates a successful restriction result, indicating that no restriction was violated.
     *
     * @return a successful {@link RestrictionResult} instance
     */
    public static RestrictionResult passed() {
        return new RestrictionResult(true, Optional.empty());
    }

    /**
     * Creates a failed restriction result with the given denial reason.
     *
     * @param reason the reason why the restriction failed, may be {@code null}
     * @return a failed {@link RestrictionResult} instance
     */
    public static RestrictionResult failed(RestrictionDenyReason reason) {
        return new RestrictionResult(false, Optional.ofNullable(reason));
    }
}
