package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object DuelsBlockTrail : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage == LanguageData.DUELS_BLOCK_TRAIL) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeDuelsBlockTrailDisabled
    override fun getPriority() = -1
}

