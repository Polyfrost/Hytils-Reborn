package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object BroadcastLevelUp : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.contains(": ")) return
        LanguageData.LEVEL_UP.find(event.unformattedMessage)?.let { match ->
            val level = match.groups["level"]?.value ?: return
            OmniClientChatSender.queue("/gc Level up! I am now Hypixel Level $level!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.broadcastLevelUp
}
