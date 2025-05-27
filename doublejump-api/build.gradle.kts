group = "com.github.imdmk"
version = "2.1.6"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}
