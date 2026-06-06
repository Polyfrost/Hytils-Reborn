package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import java.util.*

// TODO: notifications
object SilentRemoval : ChatReceiveModule {
    val removalQueue = mutableSetOf<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        val match = LanguageData.PLAYER_CONNECTION_STATUS.find(event.unformattedMessage) ?: return

        val status = match.groups["status"]?.value ?: return
        if (status != "left") return

        val player = match.groups["player"]?.value ?: return
        if (removalQueue.contains(player.lowercase(Locale.ROOT))) {
            ChatUtils.sendMessage("/f remove $player")
//            Notifications.enqueue(
//                Notifications.Type.Warning,
//                HytilsRebornConstants.NAME,
//                "Silently removed $player from your friends list."
//            )
            removalQueue.remove(player)
        }
    }
}
