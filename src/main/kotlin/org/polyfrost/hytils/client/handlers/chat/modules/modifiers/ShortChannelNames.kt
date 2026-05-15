package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.PlainTextContents
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

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
                var modifiedSibling = sibling

                val siblingMatch = regex.find(sibling.string)
                if (siblingMatch != null) {
                    modifiedSibling = Component.literal("$prefix > ${siblingMatch.groupValues[1]}")
                }

                message.append(modifiedSibling)
            }
        } else {
            message = Component.literal("$prefix > ")
            event.message.siblings.forEach { message.append(it) }
        }

        event.message = message
    }
}
