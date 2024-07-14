@file:Suppress("PropertyName")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.org/releases") // Adds the Polyfrost maven repository to get Polyfrost Gradle Toolkit
    }
    plugins {
        val pgtVersion = "0.6.5" // Sets the default versions for Polyfrost Gradle Toolkit
        id("org.polyfrost.multi-version.root") version pgtVersion
    }
}

val mod_name: String by settings

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

// Adds all of our build target versions to the classpath if we need to add version-specific code.
listOf(
        "1.8.9-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}