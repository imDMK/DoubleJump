plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.imdmk"
version = "2.1.2"

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
