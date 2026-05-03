package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoPartyWarpConfirm : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.isOverlay && event.unformattedMessage == LanguageData.PARTY_CONFIRM_WARP) {
            event.cancelled = true
            Multithreading.schedule({ OmniClientChatSender.send("/p warp") }, 1500L, TimeUnit.MILLISECONDS)
        }
    }

    override fun isEnabled() = HytilsRebornConfig.autoPartyWarpConfirm
}
