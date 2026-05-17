package org.polyfrost.hytils.client.features.game

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe

object HardcoreStatus {
    private var isHardcore = false

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load?) {
        if (this.isHardcore) {
            this.isHardcore = false
        }
    }

    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (event.unformattedTitle == "Your Mini Wither died!"
            || event.unformattedTitle == "Your Wither died!"
            || event.unformattedSubtitle == "You will no longer respawn!"
        ) {
            isHardcore = true
        }
    }

    @Subscribe
    fun onChatReceive(event: ChatReceiveEvent) {
        val message = event.unformattedMessage
        if (message == "YOUR WITHER IS DEAD"
            || message.startsWith("BED DESTRUCTION > Your Bed was")
            || message == "All beds have been destroyed!"
        ) {
            isHardcore = true
        }
    }

    @JvmStatic
    fun shouldChangeStyle(): Boolean {
        return HytilsRebornConfig.hardcoreHearts && this.isHardcore
    }
}
