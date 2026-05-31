package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.PlainTextContents
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object ShortChannelNames : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        restyle(event, LanguageData.PARTY_CHANNEL, "§9P")
        restyle(event, LanguageData.GUILD_CHANNEL, "§2G")
        restyle(event, LanguageData.FRIEND_CHANNEL, "§aF")
        restyle(event, LanguageData.OFFICER_CHANNEL, "§3O")
    }

    override val isEnabled
        get() = HytilsRebornConfig.shortChannelNames
    override val priority = 3

    private fun restyle(event: ChatReceiveEvent, regex: Regex, prefix: String) {
        if (!regex.containsMatchIn(event.plainMessage)) return

        var message: Component
        if ((event.message.contents as? PlainTextContents)?.text().isNullOrEmpty()) {
            message = Component.empty()
            for (sibling in event.message.siblings) {
                val modifiedSibling = regex.find(sibling.string)?.let {
                    Component.literal("$prefix > ${it.groupValues[1]}")
                } ?: sibling

                message.append(modifiedSibling)
            }
        } else {
            message = Component.literal("$prefix > ")
            event.message.siblings.forEach { message.append(it) }
        }

        event.message = message
    }
}
