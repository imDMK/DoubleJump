package com.github.imdmk.doublejump.database;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;


@Header({
        "# ",
        "# DoubleJump Premium - Database Configuration",
        "# Configure the database connection settings below.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class DatabaseConfiguration extends ConfigSection {

    @Comment({
            "# Database mode to use.",
            "# Supported: SQLITE, MYSQL"
    })
    public DatabaseMode databaseMode = DatabaseMode.SQLITE;

    @Comment("# Database hostname or IP address")
    public String hostname = "localhost";

    @Comment("# Database name")
    public String database = "database";

    @Comment("# Database username")
    public String username = "root";

    @Comment("# Database password")
    public String password = "ExamplePassword1101";

    @Comment("# Database port")
    public int port = 3306;

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {};
    }

    @Override
    public @NotNull String getFileName() {
        return "databaseConfiguration.yml";
    }
}
