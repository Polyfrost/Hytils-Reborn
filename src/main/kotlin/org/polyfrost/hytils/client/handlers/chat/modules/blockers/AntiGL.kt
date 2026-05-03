package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object AntiGL : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return

        val message = event.unformattedMessage
        if (!HypixelUtils.getLocation().inGame()
            || (message.startsWith("-") && message.endsWith("-"))
            || (message.startsWith("▬") && message.endsWith("▬"))
            || (message.startsWith("≡") && message.endsWith("≡"))
            || !message.contains(": ")
            || message.contains(mc.user.name, true)
        ) return

        if (message.contains(LanguageData.GL_MESSAGES)) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.antiGL
    override fun getPriority() = -1
}
