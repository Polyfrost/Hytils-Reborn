package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object SkyblockWelcomeRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage == LanguageData.SKYBLOCK_WELCOME) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeSkyblockWelcome
    override fun getPriority() = -1
}

