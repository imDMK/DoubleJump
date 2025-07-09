group = "com.github.imdmk"
version = "1.0.1"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}
