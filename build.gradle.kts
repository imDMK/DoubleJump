plugins {
    `java-library`
    `maven-publish`
}

allprojects {
    apply(plugin="java-library")
    apply(plugin="maven-publish")

    java {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                from(components["java"])
            }
        }
    }
}
