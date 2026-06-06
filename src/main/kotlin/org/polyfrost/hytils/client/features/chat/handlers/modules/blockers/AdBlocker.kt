package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object AdBlocker : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.unformattedMessage
        if ((message.startsWith("-") && message.endsWith("-"))
            || (message.startsWith("▬") && message.endsWith("▬"))
            || (message.startsWith("≡") && message.endsWith("≡"))
            || !message.contains(": ")
            || message.contains(mc.user.name, true)
        ) return

        if (message.contains(LanguageData.CHAT_ADVERTISEMENTS)
            || (!HypixelUtils.getLocation().inGame() && message.contains(LanguageData.CHAT_RANK_BEGGING))
        ) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removePlayerAds
    override val priority = -1
}
