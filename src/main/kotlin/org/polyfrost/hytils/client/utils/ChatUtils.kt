package org.polyfrost.hytils.client.utils

import net.minecraft.network.chat.Component
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object ChatUtils {
    fun sendMessage(message: String) {
        mc.player?.connection?.sendChat(message)
    }

    fun displayMessage(message: Component) {
        //~ if <26.1 'sendSystemMessage' -> 'displayClientMessage'
        mc.player?.sendSystemMessage(message, /*? if <26.1 {*//*false *//*?}*/)
    }
}
