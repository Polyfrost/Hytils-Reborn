package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.ui.v1.Notifications
import java.util.Locale

object SilentRemoval : ChatReceiveModule {
    val removalQueue = mutableSetOf<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        val match = LanguageData.PLAYER_CONNECTION_STATUS.find(event.unformattedMessage) ?: return

        val status = match.groups["status"]?.value ?: return
        if (status != "left") return

        val player = match.groups["player"]?.value ?: return
        if (removalQueue.contains(player.lowercase(Locale.ROOT))) {
            OmniClientChatSender.send("/f remove $player") // FIXME: .queue
            Notifications.enqueue(
                Notifications.Type.Warning,
                HytilsRebornConstants.NAME,
                "Silently removed $player from your friends list."
            )
            removalQueue.remove(player)
        }
    }
}
