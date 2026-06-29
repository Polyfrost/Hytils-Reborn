package org.polyfrost.hytils.client.features.chat.enhancements.lines

import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatGraphics
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatLineParser
import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine
import org.polyfrost.hytils.client.utils.getFirstColor

data class SeparatorLine(val lineColor: Int, val thickness: Int) : CustomChatLine {
    override fun render(graphics: ChatGraphics, lineX: Int, lineWidth: Int, lineHeight: Int, textY: Int, textAlpha: Float) {
        val separatorY = textY + (graphics.lineHeight() - thickness) / 2

        //~ if <1.21.8 'textAlpha' -> '(textAlpha * 255).toInt()'
        val textColor = ARGB.color(textAlpha, lineColor)
        val shadowColor = ARGB.scaleRGB(textColor, 0.25f)

        graphics.fill(lineX + 1, separatorY + 1, lineX + lineWidth, separatorY + thickness + 1, shadowColor)
        graphics.fill(lineX, separatorY, lineX + lineWidth - 1, separatorY + thickness, textColor)
    }

    companion object : ChatLineParser {
        const val THICKNESS = 1
        const val WIDE_THICKNESS = 2

        override fun parse(text: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<SeparatorLine>? {
            if (trimmed.length < 5) return null

            val isNormal = trimmed.all { it == '-' || it == '—' }
            val isWide = trimmed.all { it == '▬' }
            if (!isNormal && !isWide) return null

            val color = text.getFirstColor()?.value?.let { ARGB.opaque(it) } ?: -1

            return listOf(SeparatorLine(color, if (isWide) WIDE_THICKNESS else THICKNESS))
        }

        override val isEnabled: Boolean
            get() = HytilsRebornConfig.cleanSeparatorLines
    }
}
