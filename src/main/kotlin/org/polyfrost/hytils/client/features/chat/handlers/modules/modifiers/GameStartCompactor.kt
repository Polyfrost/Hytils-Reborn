package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.mixin.client.accessor.ChatComponentAccessor
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object GameStartCompactor : ChatReceiveModule {
    var lastMessage: Component? = null

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!event.unformattedMessage.matches(LanguageData.GAME_STARTING)) return

        if (lastMessage != null) {
            //~ if <26.2 'gui.hud' -> 'gui' {
            val chat = (mc.gui.hud.chat as ChatComponentAccessor)
            val removed = chat.allMessages.removeIf { it.content == lastMessage }

            if (removed) {
                val scrollbarPos = chat.chatScrollbarPos
                val newMessageSinceScroll = chat.newMessageSinceScroll

                chat.chatScrollbarPos = 0
                chat.invokeRefreshTrimmedMessages()

                chat.chatScrollbarPos = scrollbarPos
                mc.gui.hud.chat.scrollChat(-1)
                chat.newMessageSinceScroll = newMessageSinceScroll
            }
            //~}
        }

        lastMessage = event.message
    }

    override val isEnabled
        get() = HytilsRebornConfig.compactGameStartAnnouncements

    // this should run after game status restyler
    override val priority = 2
}
