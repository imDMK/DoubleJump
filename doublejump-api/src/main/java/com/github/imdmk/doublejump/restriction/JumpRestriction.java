package com.github.imdmk.doublejump.restriction;

import java.util.List;

public record JumpRestriction(JumpRestrictionType type, List<String> list) {

    public boolean isAllowed(String value) {
        return switch (this.type) {
            case BLACKLIST -> !this.list.contains(value);
            case WHITELIST -> this.list.contains(value);
        };
    }
}
