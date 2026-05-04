plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.11" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            string(current.parsed < "1.21.11") {
                replace("Identifier", "ResourceLocation")
                replace("GameIdentifiersData", "GameIdentifiersData")
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}
