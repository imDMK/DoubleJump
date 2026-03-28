import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("net.minecrell.plugin-yml.bukkit")
    id("com.gradleup.shadow")
}

open class DoubleJumpShadowExtension {

    internal var bukkitAction: Action<BukkitPluginDescription>? = null
    internal var shadowAction: Action<ShadowJar>? = null

    fun pluginYml(action: Action<BukkitPluginDescription>) {
        bukkitAction = action
    }

    fun shadowJar(action: Action<ShadowJar>) {
        shadowAction = action
    }
}

extensions.create("doubleJumpShadow", DoubleJumpShadowExtension::class.java)

afterEvaluate {

    val ext = extensions.getByType(DoubleJumpShadowExtension::class.java)

    ext.bukkitAction?.let { action ->
        extensions.configure<BukkitPluginDescription>("bukkit") {
            action.execute(this)
        }
    }

    tasks.withType<ShadowJar>().configureEach {
        ext.shadowAction?.execute(this)
    }
}