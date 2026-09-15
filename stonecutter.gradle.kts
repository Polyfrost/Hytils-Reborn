plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            string(current.parsed < "26.3") {
                replace("com.mojang.renderpearl.api.pipeline.BlendFunction", "com.mojang.blaze3d.pipeline.BlendFunction")
                replace("com.mojang.renderpearl.api.pipeline.ColorTargetState", "com.mojang.blaze3d.pipeline.ColorTargetState")
                replace("com.mojang.renderpearl.api.pipeline.CompareOp", "com.mojang.blaze3d.platform.CompareOp")
                replace("com.mojang.renderpearl.api.pipeline.DepthStencilState", "com.mojang.blaze3d.pipeline.DepthStencilState")
                replace("com.mojang.renderpearl.api.pipeline.RenderPipeline", "com.mojang.blaze3d.pipeline.RenderPipeline")
            }

            string(current.parsed < "26.1") {
                replace("GuiGraphicsExtractor", "GuiGraphics")
                replace("ClientCommands", "ClientCommandManager")
            }

            string(current.parsed < "1.21.11") {
                replace("Identifier", "ResourceLocation")
                replace("GameIdentifiersData", "GameIdentifiersData") // stops the Identifier swap from mangling this name
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}
