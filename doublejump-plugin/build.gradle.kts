plugins {
    `doublejump-java`
    `doublejump-repositories`
    `doublejump-shadow`

    id("xyz.jpenilla.run-paper") version "3.0.2"
}

dependencies {
    implementation("com.alessiodp.libby:libby-bukkit:${Versions.LIBBY_BUKKIT}")
    api(project(":doublejump-core"))
}

tasks.build {
    dependsOn(tasks.test)
    dependsOn(tasks.shadowJar)
}

doubleJumpShadow {
    pluginYml {
        name = "DoubleJump"
        version = project.version.toString()
        apiVersion = "1.21"
        softDepend = listOf("WorldGuard", "EternalCombat")
        main = "com.github.imdmk.doublejump.plugin.DoubleJumpPlugin"
        author = "imDMK (dominiks8318@gmail.com)"
        description = "Game-changing double jump mechanics. Feels native. Lag-free performance. Fully customizable, lag-free double jumping."
        website = "https://github.com/imDMK/DoubleJump"
    }

    shadowJar {
        archiveFileName.set("DoubleJump v${project.version} (MC 1.21.x).jar")

        mergeServiceFiles()

        exclude(
            "META-INF/*.SF",
            "META-INF/*.DSA",
            "META-INF/*.RSA",
            "module-info.class",
            "org/intellij/lang/annotations/**",
            "org/jetbrains/annotations/**"
        )

        val relocationPrefix = "com.github.imdmk.doublejump.lib"
        listOf(
            "org.bstats",
        ).forEach { pkg ->
            relocate(pkg, "$relocationPrefix.$pkg")
        }
    }
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
        downloadPlugins.modrinth("WorldGuard", Versions.WORLDGUARD)
        downloadPlugins.modrinth("WorldEdit", Versions.WORLDEDIT)
        downloadPlugins.modrinth("PacketEvents", "${Versions.PACKETEVENTS}+spigot")
        downloadPlugins.modrinth("EternalCombat", Versions.ETERNAL_COMBAT)
    }
}