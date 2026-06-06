package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object HypeLimitReminderRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.startsWith(LanguageData.HYPE_LIMIT)) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeHypeLimitReminder
    override val priority = -1
}

