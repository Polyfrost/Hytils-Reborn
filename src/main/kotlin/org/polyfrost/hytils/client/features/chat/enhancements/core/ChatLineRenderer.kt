package org.polyfrost.hytils.client.features.chat.enhancements.core

import net.minecraft.util.FormattedCharSequence

//? if <1.21.11 {
/*import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Style
*///?}

fun interface ChatLineRenderer {
    fun render(graphics: ChatGraphics, sequence: FormattedCharSequence, lineX: Int, lineWidth: Int, lineHeight: Int, textY: Int, textAlpha: Float)

    //? if <1.21.11
    //fun getStyleAt(sequence: FormattedCharSequence, mouseX: Int, chatWidth: Int, font: Font): Style? = null
}
