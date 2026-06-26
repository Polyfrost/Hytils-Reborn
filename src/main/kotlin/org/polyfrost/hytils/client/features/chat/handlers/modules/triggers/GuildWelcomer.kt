package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

object GuildWelcomer : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        LanguageData.GUILD_JOIN.find(event.unformattedMessage)?.let { match ->
            val player = match.groups["player"]?.value ?: return
            ChatUtils.queueMessage("/gc Welcome to the guild $player!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.guildWelcomeMessage
}
