package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object AutoGL : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.unformattedMessage.trim()
        if (!message.contains(": ") && message.endsWith("The game starts in 5 seconds!")) {
            mc.player?.connection?.sendChat("/ac ${HytilsRebornConfig.autoGLMessage}")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoGL
}
