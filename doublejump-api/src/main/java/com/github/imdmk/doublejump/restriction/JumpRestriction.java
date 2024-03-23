package com.github.imdmk.doublejump.restriction;

import java.util.List;
import java.util.Set;

public record JumpRestriction(JumpRestrictionType type, List<String> list) {

    public boolean isAllowed(String value) {
        return switch (this.type) {
            case BLACKLIST -> !this.list.contains(value);
            case WHITELIST -> this.list.contains(value);
        };
    }

    public boolean isAllowed(Set<String> values) {
        if (values.isEmpty() && this.type == JumpRestrictionType.BLACKLIST) {
            return true;
        }

        for (String value : values) {
            return this.isAllowed(value);
        }

        return false;
    }
}
