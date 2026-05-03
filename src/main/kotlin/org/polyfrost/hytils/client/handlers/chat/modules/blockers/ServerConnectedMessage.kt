package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object ServerConnectedMessage : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage.matches(LanguageData.SERVER_CONNECTED)) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removedServerConnectedMsgs
    override fun getPriority() = -1
}
