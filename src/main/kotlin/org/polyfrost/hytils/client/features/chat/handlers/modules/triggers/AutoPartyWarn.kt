package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object AutoPartyWarn : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.startsWith("A kick")) {
            val message = if (HytilsRebornConfig.notifyWhenKickInCaps) {
                "REQUEUE, I'VE BEEN KICKED!"
            } else {
                "I've been kicked, please requeue!"
            }
            val separator = "-".repeat(9)

            OmniClientChatSender.send("/pc $separator$message$separator")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.notifyWhenKick
}
