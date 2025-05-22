plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "checkstyle")

    repositories {
        mavenCentral()

        maven("https://storehouse.okaeri.eu/repository/maven-public/")
    }

    dependencies {
        implementation("org.jetbrains:annotations:26.0.2")

        implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.5")
        implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.5")

        testImplementation(platform("org.junit:junit-bom:5.12.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    checkstyle {
        toolVersion = "10.21.0"
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
}
