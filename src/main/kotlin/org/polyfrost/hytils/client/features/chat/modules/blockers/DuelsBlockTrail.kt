package org.polyfrost.hytils.client.features.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.ChatReceiveModule

object DuelsBlockTrail : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage == LanguageData.DUELS_BLOCK_TRAIL) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeDuelsBlockTrailDisabled
    override val priority = -1
}

