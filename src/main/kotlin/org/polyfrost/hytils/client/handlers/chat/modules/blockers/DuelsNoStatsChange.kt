package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object DuelsNoStatsChange : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage.matches(LanguageData.DUELS_NO_STATS_CHANGE)) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeDuelsNoStatsChange
    override fun getPriority() = -1
}

