@file:Suppress("PropertyName")

import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Releases
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()

        // Default
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version("2.0.0")
        id("dev.deftu.gradle.multiversion-root") version("2.18.1")
    }
}

val projectName: String = extra["mod.name"]?.toString()
    ?: throw MissingPropertyException("mod.name has not been set.")

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = projectName
rootProject.buildFileName = "root.gradle.kts"

// Adds all of our build target versions to the classpath if we need to add version-specific code.
// Update this list if you want to remove/add a version and/or mod loader.
// The format is: version-modloader (f.ex: 1.8.9-forge, 1.17.1-fabric, etc)
// **REMEMBER TO ALSO UPDATE THE `root.gradle.kts` AND `build.gradle.kts` FILES WITH THE NEW VERSION(S).
listOf(
    "1.8.9-forge",
    "1.8.9-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
