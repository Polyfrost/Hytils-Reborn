package org.polyfrost.hytils.client.features.chat.handlers.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule

object ColoredPlayerConnectionStatus : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val match = LanguageData.PLAYER_CONNECTION_STATUS.find(event.plainMessage) ?: return
        val status = match.groups["status"]?.value ?: return
        val isJoin = status == "joined"

        event.message = event.message.plainCopy().withStyle(event.message.style)
            .append(event.message.siblings.first())
            .append(Component.literal(status).withStyle(if (isJoin) ChatFormatting.GREEN else ChatFormatting.RED))
            .append(Component.literal(".").withStyle(ChatFormatting.YELLOW))
    }

    override val isEnabled
        get() = HytilsRebornConfig.coloredStatuses
}
