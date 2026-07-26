package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.PlainTextContents
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object ShortPMChannelNames : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val type = LanguageData.PRIVATE_MESSAGE.matchEntire(event.plainMessage)
            ?.groups?.get("type")?.value ?: return
        val isOutgoing = type == "To"
        val channelName = "$type "

        val rootText = (event.message.contents as? PlainTextContents)?.text() ?: return
        val parts = buildList {
            if (rootText.isNotEmpty()) add(Component.literal(rootText).withStyle(event.message.style))
            addAll(event.message.siblings)
        }

        val message = Component.empty().withStyle(event.message.style).append(
            Component.literal("PM ${if (isOutgoing) ">" else "<"} ")
                .withStyle(if (isOutgoing) ChatFormatting.LIGHT_PURPLE else ChatFormatting.DARK_PURPLE)
        )

        var shortened = false
        for (part in parts) {
            if (!shortened && part.siblings.isEmpty() && part.string.startsWith(channelName)) {
                shortened = true

                val remainder = part.string.removePrefix(channelName)
                if (remainder.isNotEmpty()) message.append(Component.literal(remainder).withStyle(part.style))
            } else {
                message.append(part)
            }
        }

        event.message = message
    }

    override val isEnabled
        get() = HytilsRebornConfig.shortPMChannelNames
    override val priority = 3
}
