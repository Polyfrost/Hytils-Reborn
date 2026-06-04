plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.1" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            string(current.parsed < "26.1") {
                replace("GuiGraphicsExtractor", "GuiGraphics")
                replace("ClientCommands", "ClientCommandManager")
            }

            string(current.parsed < "1.21.11") {
                replace("Identifier", "ResourceLocation")
                replace("GameIdentifiersData", "GameIdentifiersData") // needed to prevent "GameResourceLocationsData"
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}
