plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    "1.8.9-fabric"(10809, "yarn") {
        "1.8.9-forge"(10809, "srg") {}
    }
}