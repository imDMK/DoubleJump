package com.github.imdmk.doublejump.restriction;

import java.util.List;

public record JumpRestriction<T>(JumpRestrictionType type, List<T> list) {

    public boolean isAllowed(T value) {
        return switch (this.type) {
            case BLACKLIST -> !this.list.contains(value);
            case WHITELIST -> this.list.contains(value);
        };
    }
}
