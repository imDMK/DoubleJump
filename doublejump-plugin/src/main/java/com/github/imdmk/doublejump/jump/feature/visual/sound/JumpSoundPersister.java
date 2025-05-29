package com.github.imdmk.doublejump.jump.feature.visual.sound;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.sql.SQLException;

public class JumpSoundPersister extends BaseDataType {

    private static final JumpSoundPersister INSTANCE = new JumpSoundPersister();

    private JumpSoundPersister() {
        super(SqlType.LONG_STRING, new Class<?>[] { JumpSoundPersister.class });
    }

    public static JumpSoundPersister getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        JumpSound sound = (JumpSound) javaObject;

        return String.format("%s:%s:%s", sound.sound().getKey().getKey(), sound.volume(), sound.pitch());
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String arg = (String) sqlArg;
        String[] params = arg.split(":");
        if (params.length != 3) {
            throw new IllegalArgumentException("Invalid string format: " + arg);
        }

        try {
            Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(params[0]));
            float volume = Float.parseFloat(params[1]);
            float pitch = Float.parseFloat(params[2]);

            return new JumpSound(sound, volume, pitch);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid string format: " + arg, e);
        }
    }
}

