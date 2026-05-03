package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object WhitePrivateMessages : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.plainMessage.matches(LanguageData.PRIVATE_MESSAGE)) {
            val message = event.message.plainCopy().withStyle(event.message.style)

            event.message.siblings.dropLast(2).forEach { message.append(it) }
            event.message.siblings.takeLast(2).forEach {
                var sibling = it
                if (HytilsRebornConfig.whitePrivateMessages && it.style.color == TextColor.fromLegacyFormat(ChatFormatting.GRAY)) {
                    sibling = it.copy().withStyle(ChatFormatting.WHITE)
                }
                message.append(sibling)
            }

            event.message = message
        }
    }

    override fun isEnabled() = HytilsRebornConfig.whitePrivateMessages
}
