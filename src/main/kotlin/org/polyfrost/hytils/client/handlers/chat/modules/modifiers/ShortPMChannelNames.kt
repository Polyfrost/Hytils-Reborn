package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.PlainTextContents
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object ShortPMChannelNames : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.plainMessage.matches(LanguageData.PRIVATE_MESSAGE)) return

        val type = (event.message.contents as? PlainTextContents)?.text() ?: return
        val isOutgoing = type.trim() == "To"

        val player = event.message.siblings[0]
        val colon = event.message.siblings[1]
        val message = event.message.siblings[2]

        event.message = Component.literal("PM ${if (isOutgoing) ">" else "<"} ")
            .withStyle(if (isOutgoing) ChatFormatting.LIGHT_PURPLE else ChatFormatting.DARK_PURPLE)
            .append(player).append(colon).append(message)
    }

    override val isEnabled
        get() = HytilsRebornConfig.shortPMChannelNames
    override val priority = 3
}
