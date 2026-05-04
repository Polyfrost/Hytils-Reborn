pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
        maven("https://jitpack.io/")
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.deftu.dev/snapshots")
        maven("https://maven.architectury.dev")
        maven("https://repo.polyfrost.org/releases")
        maven("https://repo.polyfrost.org/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.3"
}

stonecutter {
    create(rootProject) {
        versions("1.21.8", "1.21.11")

        vcsVersion = "1.21.11"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = providers.gradleProperty("mod.name").get()
