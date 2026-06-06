package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object DiscordSafetyWarningRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.contains(LanguageData.DISCORD_SAFETY_WARNING)) {
            val message = event.message.copy()
            message.siblings.removeIf { it.string == LanguageData.DISCORD_SAFETY_WARNING }
            // trim any newlines/whitespace-only siblings left behind
            while (message.siblings.isNotEmpty() && message.siblings.last().string.isBlank()) {
                message.siblings.removeLast()
            }
            event.message = message
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeDiscordSafetyWarning
    override val priority = -1
}

