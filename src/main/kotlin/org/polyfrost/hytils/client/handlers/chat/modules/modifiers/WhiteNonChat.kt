package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object WhiteNonChat : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.matches(LanguageData.NON_MESSAGE)
            && !event.plainMessage.matches(LanguageData.PRIVATE_MESSAGE)
        ) {
            var foundStart = false
            val message = event.message.plainCopy().withStyle(event.message.style)
            for (sibling in event.message.siblings) {
                var modifiedSibling = sibling

                if (sibling.string.endsWith("§7")) {
                    foundStart = true
                } else if (foundStart && sibling.style.color == TextColor.parseColor("#AAAAAA").getOrThrow()) {
                    modifiedSibling = sibling.copy().withStyle(ChatFormatting.WHITE)
                }

                message.append(modifiedSibling)
            }
            event.message = message
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.whiteChat
}
