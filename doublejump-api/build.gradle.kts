plugins {
    id("java-library")

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
}

group = "com.github.imdmk"
version = "2.1.0"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

checkstyle {
    toolVersion = "10.12.1"

    configFile = file("${rootDir}/checkstyle.xml")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}