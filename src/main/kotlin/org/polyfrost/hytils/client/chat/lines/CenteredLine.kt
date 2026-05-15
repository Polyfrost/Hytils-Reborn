package org.polyfrost.hytils.client.chat.lines

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ComponentRenderUtils
import net.minecraft.locale.Language
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.chat.ChatEnhancements
import org.polyfrost.hytils.client.chat.core.ChatGraphics
import org.polyfrost.hytils.client.chat.core.ChatLineParser
import org.polyfrost.hytils.client.chat.core.ChatLineRenderer
import org.polyfrost.hytils.client.chat.core.LineAlignment
import org.polyfrost.hytils.client.utils.ComponentUtils.subText
import org.polyfrost.hytils.client.utils.ComponentUtils.trim
import org.polyfrost.hytils.client.utils.StringUtils.findFirstDifferentChar
import org.polyfrost.hytils.client.utils.StringUtils.findLastDifferentChar
import kotlin.math.abs
import kotlin.math.max

data class CenteredLine(
    val sequence: FormattedCharSequence,
    val sideDecorations: SideDecorations? = null
) : ChatLineRenderer {
    override fun render(graphics: ChatGraphics, sequence: FormattedCharSequence, lineX: Int, lineWidth: Int, textY: Int, textAlpha: Float) {
        val center = lineX + (lineWidth / 2)
        graphics.drawCenteredString(this.sequence, center, textY, textAlpha)

        sideDecorations?.let { decorations ->
            val leftX = lineX + (lineWidth - decorations.centerWidth) / 2 - graphics.width(decorations.left) - SPACING
            val decorationY = textY + decorations.alignment.getVerticalOffset(graphics.lineHeight())
            graphics.drawString(decorations.left, leftX, decorationY, textAlpha)
            graphics.drawString(decorations.right, lineX + (lineWidth + decorations.centerWidth) / 2 + SPACING, decorationY, textAlpha)
        }
    }

    //? if <1.21.11 {
    /*override fun getStyleAt(sequence: FormattedCharSequence, mouseX: Int, chatWidth: Int, font: Font): net.minecraft.network.chat.Style? {
        sideDecorations?.let { decorations ->
            val leftX = (chatWidth - decorations.centerWidth) / 2 - font.width(decorations.left) - SPACING
            val rightX = (chatWidth + decorations.centerWidth) / 2 + SPACING

            if (mouseX >= leftX && mouseX < leftX + font.width(decorations.left)) {
                return font.splitter.componentStyleAtWidth(decorations.left, mouseX - leftX)
            }

            if (mouseX >= rightX && mouseX < rightX + font.width(decorations.right)) {
                return font.splitter.componentStyleAtWidth(decorations.right, mouseX - rightX)
            }
        }

        val textX = (chatWidth - font.width(sequence)) / 2
        if (mouseX < textX) return null

        return font.splitter.componentStyleAtWidth(sequence, mouseX - textX)
    }
    *///?}

    companion object : ChatLineParser {
        private const val SPACING = 4
        private val CHEVRONS = charArrayOf('<', '>')
        private val IS_CHEVRON = { c: Char -> c in CHEVRONS }

        override fun parse(text: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<ChatLineParser.ParsedLine>? {
            if (!raw.startsWith(" ") || trimmed.isEmpty()) return null
            val firstChar = raw.findFirstDifferentChar(0, ' ')
            val lastChar = raw.findLastDifferentChar(raw.length - 1, ' ')

            if (firstChar < 0 || lastChar < 0) return null

            var middleStart = firstChar
            var middleEnd = lastChar

            var leftChevron: FormattedCharSequence? = null
            var rightChevron: FormattedCharSequence? = null

            val hasChevronLeft = IS_CHEVRON(raw[firstChar])
            val hasChevronRight = IS_CHEVRON(raw[lastChar])

            if (hasChevronLeft) {
                val chevronEnd = raw.findFirstDifferentChar(firstChar, *CHEVRONS)
                if (chevronEnd >= 0 && abs(chevronEnd - firstChar) >= 2) {
                    val j = raw.findFirstDifferentChar(chevronEnd, ' ')
                    if (j >= 0) {
                        leftChevron = text.visualOrderText.subText(firstChar, chevronEnd - 1)
                        middleStart = j
                    }
                }
            } else leftChevron = FormattedCharSequence.EMPTY

            if (hasChevronRight) {
                val chevronEnd = raw.findLastDifferentChar(lastChar, *CHEVRONS)
                if (chevronEnd >= 0 && abs(chevronEnd - lastChar) >= 2) {
                    val j = raw.findLastDifferentChar(chevronEnd, ' ')
                    if (j >= 0) {
                        rightChevron = text.visualOrderText.subText(chevronEnd + 1, lastChar)
                        middleEnd = j
                    }
                }
            } else rightChevron = FormattedCharSequence.EMPTY

            val chevrons = if ((hasChevronLeft || hasChevronRight) && leftChevron != null && rightChevron != null)
                leftChevron to rightChevron else null

            val leadingWidth = if (chevrons != null) {
                val spacingText = Language.getInstance().getVisualOrder(FormattedText.of(" ".repeat(firstChar)))
                font.width(FormattedCharSequence.composite(spacingText, chevrons.first))
            } else font.width(" ".repeat(firstChar))

            val middleText = text.subText(middleStart, middleEnd)
            val middleWidth = font.width(middleText)

            // off-center by more than 6 pixels - probably not a centered line
            if (abs(ChatEnhancements.DEFAULT_CHAT_WIDTH / 2 - (leadingWidth + middleWidth / 2)) > 6) return null

            val decorationsWidth = chevrons?.let {
                max(font.width(it.first), font.width(it.second)) * 2 + SPACING * 2
            } ?: 0

            val wrappedList = ComponentRenderUtils.wrapComponents(middleText, chatWidth - decorationsWidth, font)
                .map { it.trim() }

            val maxMiddleWidth = wrappedList.maxOfOrNull { font.width(it) } ?: middleWidth
            val even = wrappedList.size % 2 == 0
            val middleIndex = wrappedList.size / 2

            return wrappedList.mapIndexed { i, orderedText ->
                val decorations = chevrons?.takeIf {
                    i in (middleIndex - if (even) 1 else 0)..middleIndex
                }?.let { (l, r) ->
                    val alignment = if (i == middleIndex) (if (even) LineAlignment.TOP else LineAlignment.CENTER) else LineAlignment.BOTTOM
                    SideDecorations(l, r, maxMiddleWidth, alignment)
                }

                ChatLineParser.ParsedLine(
                    orderedText,
                    CenteredLine(orderedText, decorations)
                )
            }
        }

        override val isEnabled: Boolean
            get() = HytilsRebornConfig.fixCenteredMessages
    }

    data class SideDecorations(
        val left: FormattedCharSequence,
        val right: FormattedCharSequence,
        val centerWidth: Int,
        val alignment: LineAlignment
    )
}
