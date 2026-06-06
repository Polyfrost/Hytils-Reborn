package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

// TODO: notifications
object AutoFriend : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.contains(": ")) return

        val match = LanguageData.FRIEND_REQUEST.find(event.unformattedMessage) ?: return
        var player = match.groups["player"]?.value ?: return
        if (player.startsWith("[")) player = player.substringAfter("] ")

        ChatUtils.sendMessage("/friend $player")
//        Notifications.enqueue(
//            Notifications.Type.Info,
//            HytilsRebornConstants.NAME,
//            "Automatically added $player to your friends list."
//        )
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoFriend
}
