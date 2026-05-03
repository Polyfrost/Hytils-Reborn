package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object EarnedCoinsAndExpRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return

        val message = event.unformattedMessage.replace("\n", "")
        if (message.matches(LanguageData.EARNED_COINS_AND_EXP)) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.removeEarnedCoinsAndExp
    override fun getPriority() = -1
}

