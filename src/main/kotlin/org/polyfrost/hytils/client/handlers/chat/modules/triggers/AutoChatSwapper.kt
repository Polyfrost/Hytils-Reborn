package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoChatSwapper : ChatReceiveModule {
    var shouldCancelChannelMessage = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return

        if (event.plainMessage.matches(LanguageData.PARTY_JOIN)) {
            OmniClientChatSender.send("/chat party") // FIXME: .queue
            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (event.plainMessage.matches(LanguageData.PARTY_LEAVE)) {
            val channel = when (HytilsRebornConfig.chatSwapperReturnChannel) {
                1 -> "guild"
                2 -> "officer"
                else -> "all"
            }
            OmniClientChatSender.send("/chat $channel")
            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (shouldCancelChannelMessage
            && (event.plainMessage == LanguageData.ALREADY_IN_CHANNEL
                    || (HytilsRebornConfig.chatSwapperHideAllChannelMsg && event.plainMessage.matches(LanguageData.CHANNEL_SWAP)))) {
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.chatSwapper
}
