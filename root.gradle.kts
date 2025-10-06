plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    // Adding new versions/loaders can be done like so:
    // For each version, we add a new wrapper around the last from highest to lowest.
    // Each mod loader needs to link up to the previous version's mod loader so that the mappings can be processed from the previous version.
    // "1.12.2-forge"(11202, "srg") {
    //     "1.8.9-forge"(10809, "srg")
    // }

    "1.21.8-fabric"(1_21_08, "yarn") {
        "1.21.8-neoforge"(1_21_08, "srg") {
            "1.21.5-neoforge"(1_21_05, "srg") {
                "1.21.5-fabric"(1_21_05, "yarn") {
                    "1.21.4-fabric"(1_21_04, "yarn") {
                        "1.21.4-neoforge"(1_21_04, "srg") {
                            "1.21.1-neoforge"(1_21_01, "srg") {
                                "1.21.1-fabric"(1_21_01, "yarn") {
                                    "1.20.4-fabric"(1_20_04, "yarn") {
                                        "1.20.4-neoforge"(1_20_04, "srg") {
                                            "1.20.4-forge"(1_20_04, "srg") {
                                                "1.20.1-forge"(1_20_01, "srg") {
                                                    "1.20.1-fabric"(1_20_01, "yarn") {
                                                        "1.16.5-fabric"(1_16_05, "yarn") {
                                                            "1.16.5-forge"(1_16_05, "srg") {
                                                                "1.12.2-forge"(1_12_02, "srg") {
                                                                    "1.12.2-fabric"(1_12_02, "yarn") {
                                                                        "1.8.9-fabric"(1_08_09, "yarn") {
                                                                            "1.8.9-forge"(1_08_09, "srg")
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    strictExtraMappings.set(true)
}
