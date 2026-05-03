package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object CurseOfSpamRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage == LanguageData.CURSE_OF_SPAM) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeCurseOfSpam
    override fun getPriority() = -1
}
