package org.polyfrost.hytils.client.chat.core

import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

interface ChatLineParser {
    val isEnabled: Boolean

    fun parse(text: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<ParsedLine>?

    data class ParsedLine(val sequence: FormattedCharSequence, val renderer: ChatLineRenderer)
}
