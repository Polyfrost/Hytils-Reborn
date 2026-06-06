package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object WhitePrivateMessages : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.matches(LanguageData.PRIVATE_MESSAGE)) {
            val message = event.message.plainCopy().withStyle(event.message.style)

            event.message.siblings.dropLast(2).forEach { message.append(it) }
            event.message.siblings.takeLast(2).forEach {
                val sibling = if (it.style.color == TextColor.fromLegacyFormat(ChatFormatting.GRAY)) {
                    it.copy().withStyle(ChatFormatting.WHITE)
                } else it

                message.append(sibling)
            }

            event.message = message
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.whitePrivateMessages
}
