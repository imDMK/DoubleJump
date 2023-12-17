package com.github.imdmk.doublejump.jump.restriction;

import com.github.imdmk.doublejump.restriction.JumpRestriction;
import com.github.imdmk.doublejump.restriction.JumpRestrictionType;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class JumpRestrictionSerializer implements ObjectSerializer<JumpRestriction<?>> {
    
    @Override
    public boolean supports(@NonNull Class<? super JumpRestriction<?>> type) {
        return JumpRestriction.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull JumpRestriction jumpRestriction, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        JumpRestrictionType type = jumpRestriction.type();

        data.add("type", type, JumpRestrictionType.class);
        data.addCollection("restrictions", jumpRestriction.list(), Object.class);
    }

    @Override
    public JumpRestriction<?> deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        JumpRestrictionType type = data.get("type", JumpRestrictionType.class);
        List<?> restrictions = data.getAsList("restrictions", Object.class);

        return new JumpRestriction<>(type, restrictions);
    }
}
