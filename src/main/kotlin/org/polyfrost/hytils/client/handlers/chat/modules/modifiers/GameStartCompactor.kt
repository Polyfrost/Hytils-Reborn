package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.data.providers.LanguageData.removeFormattingCodes
import org.polyfrost.hytils.mixin.client.accessor.ChatComponentAccessor
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object GameStartCompactor : ChatReceiveModule {
    var lastMessage: Component? = null

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay || !event.unformattedMessage.matches(LanguageData.GAME_STARTING)) return

        if (lastMessage != null) {
            val chat = (mc.gui.chat as ChatComponentAccessor)
            chat.allMessages.removeIf { it.content == lastMessage }
            chat.trimmedMessages.removeIf { it.content.getString() == lastMessage!!.string.removeFormattingCodes() }
        }

        lastMessage = event.message
    }

    fun FormattedCharSequence.getString(): String {
        val builder = StringBuilder()
        accept { _, _, codePoint ->
            builder.appendCodePoint(codePoint)
            true
        }
        return builder.toString()
    }

    override fun isEnabled() = HytilsRebornConfig.compactGameStartAnnouncements
    // this should run after game status restyler
    override fun getPriority() = 2
}
