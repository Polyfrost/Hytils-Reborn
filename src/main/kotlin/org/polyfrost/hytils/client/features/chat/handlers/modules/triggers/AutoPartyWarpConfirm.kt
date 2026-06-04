package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoPartyWarpConfirm : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.unformattedMessage == LanguageData.PARTY_CONFIRM_WARP) {
            event.cancelled = true
            Multithreading.schedule({ ChatUtils.sendMessage("/p warp") }, 1500L, TimeUnit.MILLISECONDS)
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoPartyWarpConfirm
}
