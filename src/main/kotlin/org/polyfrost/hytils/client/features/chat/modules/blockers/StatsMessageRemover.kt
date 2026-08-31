package org.polyfrost.hytils.client.features.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.ChatReceiveModule

object StatsMessageRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.matches(LanguageData.GAME_STATS)) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeViewStats
    override val priority = -1
}

