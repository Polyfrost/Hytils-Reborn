package org.polyfrost.hytils.client.features.chat.enhancements.core

import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component

interface ChatLineParser {
    val isEnabled: Boolean

    fun parse(text: Component, raw: String, trimmed: String, chatWidth: Int, font: Font): List<CustomChatLine>?
}
