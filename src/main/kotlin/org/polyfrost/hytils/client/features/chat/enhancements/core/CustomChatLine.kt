package org.polyfrost.hytils.client.features.chat.enhancements.core

import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.FormattedCharSink

interface CustomChatLine : FormattedCharSequence {
    fun render(graphics: ChatGraphics, lineX: Int, lineWidth: Int, lineHeight: Int, textY: Int, textAlpha: Float)

    override fun accept(sink: FormattedCharSink) = false

    //? if <1.21.11
    //fun getStyleAt(mouseX: Int, chatWidth: Int, font: net.minecraft.client.gui.Font): net.minecraft.network.chat.Style? = null
}

