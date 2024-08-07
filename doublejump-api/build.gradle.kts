plugins {
    id("java-library")

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
}

group = "com.github.imdmk"
version = "2.1.5"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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
