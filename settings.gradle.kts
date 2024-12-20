import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Repositories
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

        // Default repositories
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version("2.0.0")
        id("dev.deftu.gradle.multiversion-root") version("2.13.0")
    }
}

val projectName: String = extra["mod.name"]?.toString()
    ?: throw MissingPropertyException("mod.name has not been set.")
rootProject.name = projectName
rootProject.buildFileName = "root.gradle.kts"

// Adds all of our build target versions to the classpath if we need to add version-specific code.
listOf(
    "1.8.9-forge",
    "1.8.9-fabric",
//    "1.12.2-forge",
//    "1.12.2-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}