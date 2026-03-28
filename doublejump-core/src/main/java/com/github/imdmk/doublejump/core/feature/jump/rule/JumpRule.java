package com.github.imdmk.doublejump.core.feature.jump.rule;

import com.github.imdmk.doublejump.core.feature.jump.JumpContext;

@FunctionalInterface
public interface JumpRule {
    JumpResult apply(JumpContext context);
}
