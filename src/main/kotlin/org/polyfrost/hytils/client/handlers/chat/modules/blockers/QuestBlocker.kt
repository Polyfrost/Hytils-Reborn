package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object QuestBlocker : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage.startsWith("Automatically activated:")) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeAutoQuests
    override fun getPriority() = -1
}

