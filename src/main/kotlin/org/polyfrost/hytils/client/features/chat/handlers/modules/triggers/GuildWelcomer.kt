package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object GuildWelcomer : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        LanguageData.GUILD_JOIN.find(event.unformattedMessage)?.let { match ->
            val player = match.groups["player"]?.value ?: return
            mc.player?.connection?.sendChat("/gc Welcome to the guild $player!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.guildWelcomeMessage
}
