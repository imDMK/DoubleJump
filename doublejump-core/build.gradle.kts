import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("checkstyle")
}

group = "com.github.imdmk"
version = "2.1.4"

repositories {
    mavenCentral()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

dependencies {
    implementation(project(":doublejump-api"))

    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.5")

    implementation("dev.triumphteam:triumph-gui:3.1.10")

    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.2")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.2")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.2")

    implementation("net.kyori:adventure-platform-bukkit:4.3.3")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    implementation("dev.rollczi:litecommands-bukkit:3.4.2")

    implementation("com.eternalcode:gitcheck:1.0.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

bukkit {
    name = "DoubleJump"
    version = "${project.version}"
    apiVersion = "1.17"
    main = "com.github.imdmk.doublejump.DoubleJumpPlugin"
    author = "DMK"
    description = "Efficient double jump plugin with many features and configuration possibilities"
    website = "https://github.com/imDMK/DoubleJump"
    softDepend = listOf("PlaceholderAPI", "WorldGuard")
}

checkstyle {
    toolVersion = "10.12.1"

    configFile = file("${rootDir}/checkstyle.xml")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.release = 17
}

tasks.withType<ShadowJar> {
    archiveFileName.set("DoubleJump v${project.version}.jar")

    dependsOn("checkstyleMain")
    dependsOn("checkstyleTest")
    dependsOn("test")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**"
    )

    val libPrefix = "com.github.imdmk.doublejump.lib"
    listOf(
        "dev.triumphteam",
        "dev.rollczi.litecommands",
        "org.json.simple",
        "org.bstats",
        "org.yaml",
        "net.kyori",
        "eu.okaeri",
        "com.eternalcode",
        "com.google.gson",
        "panda.std"
    ).forEach { lib ->
        relocate(lib, "$libPrefix.$lib")
    }
}