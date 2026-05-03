package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object LobbyJoinRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && !event.unformattedMessage.contains(": ") && event.unformattedMessage.contains(LanguageData.LOBBY_JOIN)) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeLobbyJoin
    override fun getPriority() = -1
}

