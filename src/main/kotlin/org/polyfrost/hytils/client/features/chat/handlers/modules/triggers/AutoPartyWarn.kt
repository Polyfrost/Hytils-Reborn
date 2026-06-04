package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object AutoPartyWarn : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.startsWith("A kick")) {
            val message = if (HytilsRebornConfig.notifyWhenKickInCaps) {
                "REQUEUE, I'VE BEEN KICKED!"
            } else {
                "I've been kicked, please requeue!"
            }
            val separator = "-".repeat(9)

            mc.player?.connection?.sendChat("/pc $separator$message$separator")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.notifyWhenKick
}
