pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.deftu.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.4"
    id("dev.kikugie.loom-back-compat") version "0.3"
}

stonecutter {
    create(rootProject) {
        versions("1.21.8", "1.21.11", "26.1")

        vcsVersion = "26.1"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}

// Configures the root project Gradle name based on the value in `stonecutter.properties.toml`
rootProject.name = sc.properties["mod.name"]
