package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object ThankWatchdog : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage == LanguageData.WATCHDOG_ANNOUNCEMENT
            || event.unformattedMessage.startsWith(LanguageData.WATCHDOG_BAN)
        ) {
            OmniClientChatSender.send("/ac Thanks Watchdog!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.thankWatchdog
}
