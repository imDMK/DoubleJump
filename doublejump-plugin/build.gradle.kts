import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.github.imdmk"
version = "2.1.5"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // SpigotMC
    maven("https://repo.eternalcode.pl/releases") // EternalCode
}

dependencies {
    implementation(project(":doublejump-api"))

    compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT")

    implementation("org.panda-lang.utilities:di:1.8.0")

    implementation("net.kyori:adventure-platform-bukkit:4.4.0")
    implementation("net.kyori:adventure-text-minimessage:4.19.0")

    implementation("com.eternalcode:multification-bukkit:1.1.4")
    implementation("com.eternalcode:multification-okaeri:1.1.4")
    implementation("com.eternalcode:gitcheck:1.0.0")

    implementation("org.bstats:bstats-bukkit:3.1.0")

    implementation("dev.rollczi:litecommands-bukkit:3.9.7")
    implementation("dev.rollczi:litecommands-annotations:3.9.7")
}

bukkit {
    name = "DoubleJump"
    version = "${project.version}"
    apiVersion = "1.17"
    main = "com.github.imdmk.doublejump.DoubleJumpPlugin"
    author = "imDMK"
    description = "Efficient double jump plugin with many features and configuration possibilities"
    website = "https://github.com/imDMK/DoubleJump"
    softDepend = listOf("PlaceholderAPI", "WorldGuard")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("DoubleJump v${project.version}.jar")

    dependsOn("checkstyleMain")
    dependsOn("checkstyleTest")
    dependsOn("test")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
    )

    val libPrefix = "com.github.imdmk.doublejump.lib"
    listOf(
        "com.eternalcode",
        "dev.rollczi",
        "eu.okaeri",
        "javassist",
        "net.kyori",
        "org.bstats",
        "org.json",
        "org.panda_lang",
        "org.yaml",
        "panda.std",
        "panda.utilities"
    ).forEach { lib ->
        relocate(lib, "$libPrefix.$lib")
    }
}