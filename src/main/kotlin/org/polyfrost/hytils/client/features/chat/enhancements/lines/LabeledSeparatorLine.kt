package org.polyfrost.hytils.client.features.chat.enhancements.lines

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ComponentRenderUtils
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import net.minecraft.util.FormattedCharSequence
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.features.chat.enhancements.ChatEnhancements
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatGraphics
import org.polyfrost.hytils.client.features.chat.enhancements.core.ChatLineParser
import org.polyfrost.hytils.client.features.chat.enhancements.core.CustomChatLine
import org.polyfrost.hytils.client.features.chat.enhancements.core.LineAlignment
import org.polyfrost.hytils.client.utils.findFirstDifferentChar
import org.polyfrost.hytils.client.utils.findLastDifferentChar
import org.polyfrost.hytils.client.utils.getFirstColor
import org.polyfrost.hytils.client.utils.subText
import org.polyfrost.hytils.client.utils.trim
import kotlin.math.abs

data class LabeledSeparatorLine(
    val sequence: FormattedCharSequence,
    val lineColor: Int,
    val middleWidth: Int,
    val alignment: LineAlignment
) : CustomChatLine {
    override fun render(graphics: ChatGraphics, lineX: Int, lineWidth: Int, lineHeight: Int, textY: Int, textAlpha: Float) {
        val center = lineX + (lineWidth / 2)
        graphics.drawCenteredString(sequence, center, textY, textAlpha)

        val start = lineX + (lineWidth - middleWidth) / 2
        val end = start + middleWidth

        val baseY = textY + (graphics.lineHeight() - SeparatorLine.THICKNESS) / 2
        val separatorY = baseY + alignment.getVerticalOffset(lineHeight)

        val textColor = ARGB.color(textAlpha, lineColor)
        val shadowColor = ARGB.scaleRGB(textColor, 0.25f)

        // left separator line
        graphics.fill(lineX + 1, separatorY + 1, start - 4, separatorY + SeparatorLine.THICKNESS + 1, shadowColor)
        graphics.fill(lineX, separatorY, start - 5, separatorY + SeparatorLine.THICKNESS, textColor)

        // right separator line
        graphics.fill(end + 5, separatorY + 1, lineX + lineWidth, separatorY + SeparatorLine.THICKNESS + 1, shadowColor)
        graphics.fill(end + 4, separatorY, lineX + lineWidth - 1, separatorY + SeparatorLine.THICKNESS, textColor)
    }

    //? if <1.21.11 {
    /*override fun getStyleAt(mouseX: Int, chatWidth: Int, font: Font): net.minecraft.network.chat.Style? {
        val textX = (chatWidth - font.width(sequence)) / 2

        if (mouseX < textX) return null
        return font.splitter.componentStyleAtWidth(sequence, mouseX - textX)
    }
    *///?}

    companion object : ChatLineParser {
        override fun parse(text: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<CustomChatLine>? {
            if (!trimmed.startsWith("-") || !trimmed.endsWith("-")) return null
            if (font.width(text) <= ChatEnhancements.DEFAULT_CHAT_WIDTH - 10) return null

            val leftSeparatorEnd = raw.findFirstDifferentChar(0, '-')
            val rightSeparatorStart = raw.findLastDifferentChar(raw.length - 1, '-')
            if (leftSeparatorEnd < 0 || rightSeparatorStart < 0) return null

            val middleText = text.subText(leftSeparatorEnd, rightSeparatorStart)
            val middleWidth = font.width(middleText)
            val leadingWidth = font.width(text.visualOrderText.subText(0, leftSeparatorEnd - 1))

            // off-center by more than 6 pixels - probably not a centered line
            if (abs(ChatEnhancements.DEFAULT_CHAT_WIDTH / 2 - (leadingWidth + middleWidth / 2)) > 6) return null

            val wrappedList = ComponentRenderUtils.wrapComponents(middleText, chatWidth, font).map { it.trim() }
            val maxMiddleWidth = wrappedList.maxOfOrNull { font.width(it) } ?: chatWidth
            val color = text.getFirstColor()?.value?.let { ARGB.opaque(it) } ?: -1

            val even = wrappedList.size % 2 == 0
            val middleIndex = wrappedList.size / 2

            return wrappedList.mapIndexed { i, sequence ->
                if (i < middleIndex - (if (even) 1 else 0) || i > middleIndex) {
                    CenteredLine(sequence)
                } else {
                    val alignment = if (i == middleIndex) (if (even) LineAlignment.TOP else LineAlignment.CENTER) else LineAlignment.BOTTOM
                    LabeledSeparatorLine(sequence, color, maxMiddleWidth, alignment)
                }
            }
        }

        override val isEnabled: Boolean
            get() = HytilsRebornConfig.cleanSeparatorLines
    }
}
