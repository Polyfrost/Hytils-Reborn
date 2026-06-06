package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object TicketMachineRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val location = HypixelUtils.getLocation()
        val message = event.unformattedMessage
        if (location.gameType.orElse(null) == GameType.BEDWARS
            && !location.inGame()
            && LanguageData.TICKET_ANNOUNCER.matches(message)
        ) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeTicketMachineAnnouncements
    override val priority = -1
}

