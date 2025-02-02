@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.utils.GameSide
import dev.deftu.gradle.utils.MinecraftVersion

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion") // Applies preprocessing for multiple versions of Minecraft and/or multiple mod loaders.
    id("dev.deftu.gradle.tools") // Applies several configurations to things such as the Java version, project name/version, etc.
    id("dev.deftu.gradle.tools.resources") // Applies resource processing so that we can replace tokens, such as our mod name/version, in our resources.
    id("dev.deftu.gradle.tools.bloom") // Applies the Bloom plugin, which allows us to replace tokens in our source files, such as being able to use `@MOD_VERSION` in our source files.
    id("dev.deftu.gradle.tools.shadow") // Applies the Shadow plugin, which allows us to shade our dependencies into our mod JAR. This is NOT recommended for Fabric mods, but we have an *additional* configuration for those!
    id("dev.deftu.gradle.tools.ducks") // Creates a ducks source set, which allows us to use theoretical classes which may not exist at runtime (such as things which are in other mods).
    id("dev.deftu.gradle.tools.minecraft.loom") // Applies the Loom plugin, which automagically configures Essential's Architectury Loom plugin for you.
    id("dev.deftu.gradle.tools.minecraft.releases") // Applies the Minecraft auto-releasing plugin, which allows you to automatically release your mod to CurseForge and Modrinth.
}

toolkitLoomHelper {
    useOneConfig {
        version = "1.0.0-alpha.62"
        loaderVersion = "1.1.0-alpha.44"

        usePolyMixin = true
        polyMixinVersion = "0.8.4+build.2"

        applyLoaderTweaker = true

        for (module in arrayOf("commands", "config", "config-impl", "events", "internal", "ui", "utils")) {
            +module
        }
    }

    // Turns off the server-side run configs, as we're building a client-sided mod.
    disableRunConfigs(GameSide.SERVER)

    // Defines the name of the Mixin refmap, which is used to map the Mixin classes to the obfuscated Minecraft classes.
    if (!mcData.isNeoForge) {
        useMixinRefMap("hytils")
    }

    if (mcData.isForge) {
        // Configures the Mixin tweaker if we are building for Forge.
        useForgeMixin("hytils")
    }
}

loom {
    if (mcData.isLegacyForge) {
        forge {
            accessTransformer(rootProject.file("src/main/resources/hytils_at.cfg"))
        }
    } else if (mcData.isLegacyFabric) {
        accessWidenerPath.set(rootProject.file("src/main/resources/hytils_aw.accesswidener"))
    }
}

dependencies {
    implementation(shade("com.github.ben-manes.caffeine:caffeine:2.9.3")!!)

    // Add Fabric Language Kotlin and (Legacy) Fabric API as dependencies (these are both optional but are particularly useful).
    if (mcData.isFabric) {
        if (mcData.isLegacyFabric) {
            // 1.8.9 - 1.13
            modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}")
        } else {
            // 1.16.5+
            modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        }
    }
}

tasks {
    processResources {
        exclude("hytils_at.cfg")
    }
}