package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoChatSwapper : ChatReceiveModule {
    var shouldCancelChannelMessage = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.matches(LanguageData.PARTY_JOIN)) {
            ChatUtils.sendMessage("/chat party")
            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (event.plainMessage.matches(LanguageData.PARTY_LEAVE)) {
            val channel = when (HytilsRebornConfig.chatSwapperReturnChannel) {
                1 -> "guild"
                2 -> "officer"
                else -> "all"
            }
            ChatUtils.sendMessage("/chat $channel")
            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (shouldCancelChannelMessage
            && (event.plainMessage == LanguageData.ALREADY_IN_CHANNEL
                || (HytilsRebornConfig.chatSwapperHideAllChannelMsg && event.plainMessage.matches(LanguageData.CHANNEL_SWAP)))
        ) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.chatSwapper
}
