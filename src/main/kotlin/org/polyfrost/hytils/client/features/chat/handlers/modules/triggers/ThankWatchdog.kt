package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

object ThankWatchdog : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage == LanguageData.WATCHDOG_ANNOUNCEMENT
            || event.unformattedMessage.startsWith(LanguageData.WATCHDOG_BAN)
        ) {
            ChatUtils.queueMessage("/ac Thanks Watchdog!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.thankWatchdog
}
