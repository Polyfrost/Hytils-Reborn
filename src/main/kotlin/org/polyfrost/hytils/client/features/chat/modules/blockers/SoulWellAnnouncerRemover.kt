package org.polyfrost.hytils.client.features.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.ChatReceiveModule

object SoulWellAnnouncerRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.matches(LanguageData.SOUL_WELL_FIND)) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeSoulWellAnnouncements
    override val priority = -1
}

