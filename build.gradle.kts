import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.fabric.loom.remap)
    alias(libs.plugins.bloom)
    alias(libs.plugins.mod.publish)
}

val modId: String = sc.properties["mod.id"]
val modName: String = sc.properties["mod.name"]
val modVersion: String = sc.properties["mod.version"]
val mcVersion = sc.current.version

version = "$modVersion+$mcVersion"
base.archivesName = modName

repositories {
    maven("https://maven.parchmentmc.org")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.gegy.dev/releases")
    maven("https://repo.hypixel.net/repository/Hypixel")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    fun <T> optionalProp(property: String, block: (String) -> T?): T? =
        findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

    minecraft("com.mojang:minecraft:$mcVersion")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        optionalProp("${property("deps.parchment")}") {
            parchment("org.parchmentmc.data:parchment-$mcVersion:$it@zip")
        }
        optionalProp("${property("deps.yalmm")}") {
            mappings("dev.lambdaurora:yalmm-mojbackward:$mcVersion+build.$it")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    modImplementation("org.polyfrost.oneconfig:$mcVersion-fabric:${property("deps.oneconfig")}")
    for (module in listOf("config", "config-impl", "events", "internal", "ui")) {
        implementation("org.polyfrost.oneconfig:$module:${property("deps.oneconfig")}")
    }

    implementation("net.hypixel:mod-api:${property("deps.hypixel_mod_api")}")
    for (module in listOf("fabric-command-api-v2", "fabric-networking-api-v1")) {
        modImplementation(fabricApi.module(module, sc.properties["deps.fabric_api"]))
    }

    // needed for height overlay compatibility
    modCompileOnly("maven.modrinth:sodium:mc$mcVersion-${property("deps.sodium")}-fabric")

    if (sc.current.parsed < "1.21.11") {
        // needed for fabric 1.21.8
        modImplementation(fabricApi.module("fabric-rendering-v1", sc.properties["deps.fabric_api"]))
        // needed for sodium height overlay compatibility
        modCompileOnly(fabricApi.module("fabric-renderer-api-v1", sc.properties["deps.fabric_api"]))
    }
}

loom {
    runConfigs.named("client").configure {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

tasks {
    processResources {
        val props = mapOf(
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_version" to modVersion,
            "mc_version" to mcVersion,
            "oneconfig_version" to sc.properties["deps.oneconfig"]
        )

        inputs.properties(props)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }

    jar {
        inputs.property("archivesName", base.archivesName)

        from("LICENSE") {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }
    }

    withType<JavaCompile>().configureEach {
        options.release.set(21)
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
}

bloom {
    replacement("@MOD_ID@", modId)
    replacement("@MOD_NAME@", modName)
    replacement("@MOD_VERSION@", modVersion)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }

// make sure modrinth.token is set in your user gradle properties
publishMods {
    file = project.tasks.remapJar.get().archiveFile

    displayName = modVersion
    version = "v$modVersion"
    changelog = project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = ALPHA

    modLoaders.add("fabric")

    dryRun = modrinthId == null

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = findProperty("modrinth.token").toString()

            minecraftVersions.add(mcVersion)

            requires("oneconfig")
        }
    }
}
