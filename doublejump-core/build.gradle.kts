plugins {
    `doublejump-java`
    `doublejump-java-test`
    `doublejump-repositories`
}

dependencies {
    compileOnlyApi("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")

    compileOnly("com.eternalcode:eternalcombat-api:${Versions.ETERNAL_COMBAT}")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:${Versions.WORLDGUARD_API}")

    implementation("org.panda-lang.utilities:di:${Versions.PANDA_DI}")
    implementation("io.github.classgraph:classgraph:${Versions.CLASSGRAPH}")

    implementation("net.kyori:adventure-platform-bukkit:${Versions.KYORI_PLATFORM_BUKKIT}")
    implementation("net.kyori:adventure-text-minimessage:${Versions.KYORI_TEXT_MINIMESSAGE}")

    implementation("com.eternalcode:multification-bukkit:${Versions.MULTIFICATION_BUKKIT}")
    implementation("com.eternalcode:multification-okaeri:${Versions.MULTIFICATION_OKAERI}")

    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_SNAKEYAML}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_SERDES_COMMONS}")

    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITECOMMANDS}")
    implementation("dev.rollczi:litecommands-annotations:${Versions.LITECOMMANDS}")

    implementation("dev.triumphteam:triumph-gui:${Versions.TRIUMPH_GUI}")
    implementation("org.bstats:bstats-bukkit:${Versions.BSTATS_BUKKIT}")
}