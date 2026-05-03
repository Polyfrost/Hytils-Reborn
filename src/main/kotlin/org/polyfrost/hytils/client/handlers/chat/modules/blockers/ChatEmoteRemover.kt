package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object ChatEmoteRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage.contains("( ﾟ◡ﾟ)/")) {
            println("================================")
            println("FOUND A CHAT EMOTE FOUND A CHAT EMOTE FOUND A CHAT EMOTE FOUND A")
            println("plainMessage: ${event.plainMessage}")
            println("unformattedMessage: ${event.unformattedMessage}")
            println("message: ${event.message}")
            println("================================")
        }

        if (event.isOverlay || !event.unformattedMessage.contains(LanguageData.MVP_EMOTES)) return

        val message = event.message.plainCopy().withStyle(event.message.style)
        for (sibling in event.message.siblings) {
            message.append(
                Component.literal(LanguageData.MVP_EMOTES.replace(sibling.string, ""))
                    .withStyle(sibling.style).plainCopy()
                    .apply { sibling.siblings.forEach { append(it) } }
            )
        }
        event.message = message
    }

    override fun isEnabled() = HytilsRebornConfig.removeChatEmotes
    override fun getPriority() = -1
}
