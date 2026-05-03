import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT"
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("dev.deftu.gradle.bloom") version "0.2.0"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

val modid = property("mod.id")
val modname = property("mod.name")
val modversion = property("mod.version")
val mcversion = stonecutter.current.version
val oneconfigversion = property("deps.oneconfig")

version = "$modversion+$mcversion"
base.archivesName = modname.toString()

repositories {
    maven("https://maven.parchmentmc.org")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.gegy.dev/releases")
    maven("https://repo.hypixel.net/repository/Hypixel")
    maven("https://maven.caffeinemc.net/releases")
}

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run" // This sets the run folder for all mc versions to the same folder. Remove this line if you want individual run folders.
    }

    runConfigs.remove(runConfigs["server"]) // Removes server run configs
}

dependencies {
    minecraft("com.mojang:minecraft:$mcversion")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        optionalProp("${property("deps.parchment")}") {
            parchment("org.parchmentmc.data:parchment-$mcversion:$it@zip")
        }
        optionalProp("${property("deps.yalmm")}") {
            mappings("dev.lambdaurora:yalmm-mojbackward:$mcversion+build.$it")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    modImplementation("org.polyfrost.oneconfig:$mcversion-fabric:$oneconfigversion")
    arrayOf("config", "config-impl", "events", "internal", "ui").forEach {
        implementation("org.polyfrost.oneconfig:$it:$oneconfigversion")
    }

    implementation("net.hypixel:mod-api:${property("deps.hypixel_mod_api")}")
    arrayOf("fabric-command-api-v2", "fabric-networking-api-v1").forEach {
        modImplementation(fabricApi.module(it, property("deps.fabric_api").toString()))
    }
    modCompileOnly("net.caffeinemc:sodium-fabric:${property("deps.sodium")}+mc$mcversion")
}

bloom {
    replacement("@MOD_ID@", modid!!)
    replacement("@MOD_NAME@", modname!!)
    replacement("@MOD_VERSION@", modversion!!)
}

tasks.processResources {
    val props = mapOf(
        "mod_id" to modid,
        "mod_name" to modname,
        "mod_version" to modversion,
        "mc_version" to mcversion,
        "loader_version" to providers.gradleProperty("deps.fabric_loader").get()
    )

    inputs.properties(props)

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }

// make sure modrinth.token is set in your user gradle properties
publishMods {
    file = project.tasks.remapJar.get().archiveFile

    displayName = modversion.toString()
    version = "v$modversion"
    changelog = project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = ALPHA

    modLoaders.add("fabric")

    dryRun = modrinthId == null

    if (modrinthId != null) {
        modrinth {
            projectId = property("publish.modrinth").toString()
            accessToken = findProperty("modrinth.token").toString()

            minecraftVersions.add(mcversion)

            requires("oneconfig")
        }
    }
}
