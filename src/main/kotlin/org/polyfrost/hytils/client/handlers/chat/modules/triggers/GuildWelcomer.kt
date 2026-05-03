package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object GuildWelcomer : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return
        LanguageData.GUILD_JOIN.find(event.unformattedMessage)?.let { match ->
            val player = match.groups["player"]?.value ?: return
            OmniClientChatSender.send("/gc Welcome to the guild $player!")
        }
    }

    override fun isEnabled() = HytilsRebornConfig.guildWelcomeMessage
}
