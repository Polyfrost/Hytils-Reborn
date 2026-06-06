package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.ChatEmotesData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object ChatEmoteReplacer : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.message.plainCopy().withStyle(event.message.style)

        var i = 0
        while (i < event.message.siblings.size) {
            val emote = ChatEmotesData.emotes.entries.firstOrNull { (_, sequence) ->
                sequence.isNotEmpty() && i + sequence.size <= event.message.siblings.size
                    && event.message.siblings.subList(i, i + sequence.size) == sequence
            }

            if (emote != null) {
                when (HytilsRebornConfig.chatEmotesReplacementMode) {
                    1 -> emote.value.forEach { message.append(it.string) }
                    2 -> message.append(emote.key)
                }
                i += emote.value.size
            } else {
                message.append(event.message.siblings[i])
                i++
            }
        }

        event.message = message
    }

    override val isEnabled
        get() = HytilsRebornConfig.replaceChatEmotes
    override val priority = -1
}
