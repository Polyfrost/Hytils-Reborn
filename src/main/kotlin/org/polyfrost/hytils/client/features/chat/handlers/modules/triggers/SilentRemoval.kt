package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.notifications.v1.Notifications
import java.util.*

object SilentRemoval : ChatReceiveModule {
    val removalQueue = mutableSetOf<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        val match = LanguageData.PLAYER_CONNECTION_STATUS.find(event.unformattedMessage) ?: return

        val status = match.groups["status"]?.value ?: return
        if (status != "left") return

        val player = match.groups["player"]?.value ?: return
        if (removalQueue.contains(player.lowercase(Locale.ROOT))) {
            ChatUtils.queueMessage("/f remove $player")
            Notifications.success(
                HytilsRebornConstants.NAME,
                "Silently removed $player from your friends list."
            )
            removalQueue.remove(player)
        }
    }
}
