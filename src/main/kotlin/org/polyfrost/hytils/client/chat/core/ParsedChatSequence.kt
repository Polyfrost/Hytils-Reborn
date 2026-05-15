package org.polyfrost.hytils.client.chat.core

import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.FormattedCharSink

class ParsedChatSequence(val sequence: FormattedCharSequence, val renderer: ChatLineRenderer) : FormattedCharSequence {
    override fun accept(formattedCharSink: FormattedCharSink): Boolean = sequence.accept(formattedCharSink)
}
