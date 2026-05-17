package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.oneconfig.api.event.v1.events.ServerJoinEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe

object GuildMOTD : ChatReceiveModule {
    var isMotd = false
    var sentMotd = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (sentMotd) return

        if (isMotd) {
            if (event.unformattedMessage.startsWith("------")) {
                isMotd = false
                sentMotd = true
            }
            event.cancelled = true
        } else if (event.unformattedMessage.startsWith("------")
            && event.unformattedMessage.contains("Guild: Message Of The Day")
        ) {
            isMotd = true
            event.cancelled = true
        }
    }

    @Subscribe
    fun onServerJoin(event: ServerJoinEvent) {
        isMotd = false
        sentMotd = false
    }

    override val isEnabled
        get() = HytilsRebornConfig.removeGuildMotd
    override val priority = -1
}
