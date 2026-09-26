package org.polyfrost.hytils.client.features.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.ChatReceiveModule

object EarnedCoinsAndExpRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.unformattedMessage.replace("\n", "")
        if (message.matches(LanguageData.EARNED_COINS_AND_EXP)) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeEarnedCoinsAndExp
    override val priority = -1
}

