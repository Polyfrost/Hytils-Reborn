plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.bloom)
    alias(libs.plugins.mod.publish)
    id("dev.kikugie.loom-back-compat")
}

val modId: String = sc.properties["mod.id"]
val modName: String = sc.properties["mod.name"]
val modVersion: String = sc.properties["mod.version"]
val mcVersion = sc.current.version

version = "$modVersion+$mcVersion"
base.archivesName = modName

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://central.sonatype.com/repository/maven-snapshots") {
        content { includeGroup("net.kyori") }
    }
    maven("https://maven.parchmentmc.org") {
        content { includeGroup("org.parchmentmc") }
    }
    maven("https://maven.gegy.dev/releases") {
        content { includeGroup("dev.lambdaurora") }
    }
    maven("https://repo.hypixel.net/repository/Hypixel") {
        content { includeGroup("net.hypixel") }
    }
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    //maven("https://maven.terraformersmc.com/releases") {
    maven("https://maven.gnomecraft.net/releases/") {
        content { includeGroup("com.terraformersmc") }
    }
}

dependencies {
    fun <T> optionalProp(property: String, block: (String) -> T?): T? =
        findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

    minecraft("com.mojang:minecraft:$mcVersion")
    if (sc.current.parsed < "26.1") {
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
    }
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    modImplementation("org.polyfrost.oneconfig:$mcVersion-fabric:${property("deps.oneconfig")}")
    for (module in listOf("config", "config-impl", "events", "utils")) {
        implementation("org.polyfrost.oneconfig:$module:${property("deps.oneconfig")}")
    }

    implementation("net.hypixel:mod-api:${property("deps.hypixel_mod_api")}")
    modImplementation("maven.modrinth:hypixel-mod-api:${property("deps.hypixel_mod_api_fabric")}")

    // needed for height overlay compatibility
    modCompileOnly("maven.modrinth:sodium:mc$mcVersion-${property("deps.sodium")}-fabric")
}

loom {
    runConfigs.named("client").configure {
        generateRunConfig = true
        runDirectory = rootProject.file("run")
    }
}

tasks {
    processResources {
        val props = mapOf(
            "mod_id" to modId,
            "mod_version" to modVersion,
            "mod_name" to modName,
            "mod_description" to sc.properties["mod.description"],
            "mc_compat" to (sc.properties.getOrNull<String>("mod.mc_compat") ?: mcVersion),
            "oneconfig_version" to sc.properties["deps.oneconfig"]
        )

        inputs.properties(props)

        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("mixins.*.json") { expand("java" to "JAVA_${requiredJava.majorVersion}") }
    }

    jar {
        inputs.property("archivesName", base.archivesName)

        from("LICENSE") {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }
    }

    register("validateChangelog") {
        description = "Validates that the changelog is written for the current version."
        group = "publishing"

        if (!changelogText.contains(modVersion)) {
            throw GradleException("Changelog for version $modVersion not found.")
        }
    }

    publishMods.configure { dependsOn("validateChangelog") }
    matching { it.name == "publishModrinth" }.configureEach { dependsOn("validateChangelog") }
}

bloom {
    replacement("@MOD_ID@", modId)
    replacement("@MOD_NAME@", modName)
    replacement("@MOD_VERSION@", modVersion)
}

sourceSets {
    val ducks = register("ducks")
    main {
        compileClasspath += ducks.get().output
        output.setResourcesDir(java.classesDirectory)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
}

kotlin {
    jvmToolchain(requiredJava.majorVersion.toInt())
}

val modrinthId = findProperty("publish.modrinth.id")?.toString()?.takeIf { it.isNotBlank() }
val changelogText = rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."

// make sure modrinth.token is set in your user gradle properties
publishMods {
    file = loomx.modJar.get().archiveFile

    displayName = modVersion
    version = "v$modVersion"
    changelog = changelogText
    type = STABLE

    modLoaders.add("fabric")

    dryRun = modrinthId == null

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = findProperty("modrinth.token").toString()

            val mcReleases = sc.properties.rawOrNull("mod:mc_releases")?.asList()?.map { it.toString() }
            minecraftVersions.addAll(mcReleases ?: listOf(mcVersion))

            requires("oneconfig", "fabric-api", "fabric-language-kotlin", "hypixel-mod-api")
        }
    }
}
