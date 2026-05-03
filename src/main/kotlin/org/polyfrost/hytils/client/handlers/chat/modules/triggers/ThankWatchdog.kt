package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object ThankWatchdog : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay &&
            (event.unformattedMessage == "[WATCHDOG ANNOUNCEMENT]"
            || event.unformattedMessage.startsWith("A player has been removed from your"))
        ) {
            OmniClientChatSender.send("/ac Thanks Watchdog!")
        }
    }

    override fun isEnabled() = HytilsRebornConfig.thankWatchdog
}
