package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object DiscordSafetyWarningRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage.contains(LanguageData.DISCORD_SAFETY_WARNING)) {
            val message = event.message.copy()
            message.siblings.removeIf { it.string == LanguageData.DISCORD_SAFETY_WARNING }
            // trim any newlines/whitespace-only siblings left behind
            while (message.siblings.isNotEmpty() && message.siblings.last().string.isBlank()) {
                message.siblings.removeLast()
            }
            event.message = message
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeDiscordSafetyWarning
    override fun getPriority() = -1
}

