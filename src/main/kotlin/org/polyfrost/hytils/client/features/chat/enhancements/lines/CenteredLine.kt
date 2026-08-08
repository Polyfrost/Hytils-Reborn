package org.polyfrost.hytils.client.features.chat.enhancements.lines

//? if <1.21.11
//import net.minecraft.client.gui.Font

import net.minecraft.util.FormattedCharSequence
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatGraphics
import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine

data class CenteredLine(val sequence: FormattedCharSequence) : CustomChatLine {
    override fun render(graphics: ChatGraphics, lineX: Int, lineWidth: Int, lineHeight: Int, textY: Int, textAlpha: Float) {
        graphics.drawCenteredString(sequence, lineX + (lineWidth / 2), textY, textAlpha)
    }

    //? if <1.21.11 {
    /*override fun getStyleAt(mouseX: Int, chatWidth: Int, font: Font): net.minecraft.network.chat.Style? {
        val textX = (chatWidth - font.width(sequence)) / 2
        if (mouseX < textX) return null

        return font.splitter.componentStyleAtWidth(sequence, mouseX - textX)
    }
    *///?}
}
