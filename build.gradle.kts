@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.minecraft.loom")
}

// Sets up the variables for when we preprocess to other Minecraft versions.
preprocess {
    vars.put("MODERN", if (mcData.version.minor >= 16) 1 else 0)
}

toolkitLoomHelper {
    // Adds OneConfig to our project
    useOneConfig("1.1.0-alpha.34", "1.0.0-alpha.47", mcData, "commands", "config-impl", "events", "hud", "internal", "ui")
    useDevAuth()

    // Removes the server configs from IntelliJ IDEA, leaving only client runs.
    // If you're developing a server-side mod, you can remove this line.
    disableRunConfigs(GameSide.SERVER)

    // Sets up our Mixin refmap naming
    if (!mcData.isNeoForge) {
        useMixinRefMap("hytils")
    }

    // Adds the tweak class if we are building legacy version of forge as per the documentation (https://docs.polyfrost.org)
    if (mcData.isLegacyForge) {
        useTweaker("org.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker", GameSide.CLIENT)
        useForgeMixin("hytils") // Configures the mixins if we are building for forge, useful for when we are dealing with cross-platform projects.
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

// Configures the output directory for when building from the `src/resources` directory.
sourceSets {
    val dummy by creating
    main {
        compileClasspath += dummy.output
        output.setResourcesDir(java.classesDirectory)
    }
}

// Adds the Polyfrost maven repository so that we can get the libraries necessary to develop the mod.
repositories {
    maven("https://repo.polyfrost.org/releases")
}

// Configures the libraries/dependencies for your mod.
dependencies {
    implementation(shade("com.github.ben-manes.caffeine:caffeine:2.9.3")!!)

    // If we are building for legacy forge, includes the launch wrapper with `shade` as we configured earlier.
    if (mcData.isLegacyForge) {
        compileOnly("org.polyfrost:polymixin:0.8.4+build.2")
    } else if (mcData.isFabric) {
        if (mcData.isLegacyFabric) {
            modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}")
        }

        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")
    }
}

tasks {
    processResources {
        exclude("hytils_at.cfg")
    }
}
