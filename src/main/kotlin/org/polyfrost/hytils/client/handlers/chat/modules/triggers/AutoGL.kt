package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object AutoGL : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return

        val message = event.unformattedMessage.trim()
        if (!message.contains(": ") && message.endsWith("The game starts in 5 seconds!")) {
            OmniClientChatSender.send("/ac ${HytilsRebornConfig.autoGLMessage}")
        }
    }

    override fun isEnabled() = HytilsRebornConfig.autoGL
}
