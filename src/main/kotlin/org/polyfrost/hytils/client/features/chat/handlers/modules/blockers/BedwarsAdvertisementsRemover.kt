package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object BedwarsAdvertisementsRemover : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val message = event.unformattedMessage
        if ((message.startsWith("-") && message.endsWith("-"))
            || (message.startsWith("▬") && message.endsWith("▬"))
            || (message.startsWith("≡") && message.endsWith("≡"))
            || !message.contains(": ")
            || message.contains(mc.user.name, true)
        ) return

        val location = HypixelUtils.getLocation()
        if (location.gameType.orElse(null) == GameType.BEDWARS
            && !location.inGame()
            && message.contains(LanguageData.CHAT_BEDWARS_ADVERTISEMENT)
        ) {
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.removePlayerBedwarsAds
    override val priority = -1
}
