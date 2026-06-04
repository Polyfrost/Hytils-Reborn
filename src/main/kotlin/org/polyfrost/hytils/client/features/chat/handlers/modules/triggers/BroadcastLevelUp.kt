package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

object BroadcastLevelUp : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage.contains(": ")) return
        LanguageData.LEVEL_UP.find(event.unformattedMessage)?.let { match ->
            val level = match.groups["level"]?.value ?: return
            ChatUtils.sendMessage("/gc Level up! I am now Hypixel Level $level!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.broadcastLevelUp
}
