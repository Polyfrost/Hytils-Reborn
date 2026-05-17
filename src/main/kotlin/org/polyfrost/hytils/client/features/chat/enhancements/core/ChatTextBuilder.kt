package org.polyfrost.hytils.client.features.chat.enhancements.core

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSink
import java.lang.StringBuilder

class ChatTextBuilder : FormattedCharSink {
    private val texts = mutableListOf(Component.empty())
    private var currentStyle: Style? = null
    private val builder = StringBuilder()
    private var currentIndex = -1

    override fun accept(index: Int, style: Style, codePoint: Int): Boolean {
        currentIndex++

        if (currentStyle == null) currentStyle = style

        if (currentStyle != style) {
            flush()
            currentStyle = style
        }

        if (codePoint == '\n'.code) {
            flush()
            texts.add(Component.empty())
            return true
        }
        builder.appendCodePoint(codePoint)
        return true
    }

    private fun flush() {
        if (builder.isNotEmpty()) {
            texts.last().append(Component.literal(builder.toString()).setStyle(currentStyle ?: Style.EMPTY))
            builder.clear()
        }
    }

    fun getTexts(): List<MutableComponent> {
        flush()
        return texts
    }
}
