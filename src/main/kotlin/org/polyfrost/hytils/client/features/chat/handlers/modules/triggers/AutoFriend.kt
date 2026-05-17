package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.api.ui.v1.Notifications

object AutoFriend : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.contains(": ")) return

        val match = LanguageData.FRIEND_REQUEST.find(event.unformattedMessage) ?: return
        var player = match.groups["player"]?.value ?: return
        if (player.startsWith("[")) player = player.substringAfter("] ")

        OmniClientChatSender.queue("/friend $player")
        Notifications.enqueue(
            Notifications.Type.Info,
            HytilsRebornConstants.NAME,
            "Automatically added $player to your friends list."
        )
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoFriend
}
