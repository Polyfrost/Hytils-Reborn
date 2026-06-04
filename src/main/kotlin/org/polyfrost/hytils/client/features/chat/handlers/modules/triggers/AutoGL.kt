package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

object AutoGL : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.unformattedMessage.trim()
        if (!message.contains(": ") && message.endsWith("The game starts in 5 seconds!")) {
            ChatUtils.sendMessage("/ac ${HytilsRebornConfig.autoGLMessage}")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoGL
}
