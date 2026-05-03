package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoGG : ChatReceiveModule {
    private var gameEnded = false
    private var shouldSend = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay || !hasGameEnded(event.unformattedMessage)) return

        shouldSend = true

        Multithreading.schedule(
            { if (shouldSend) OmniClientChatSender.send("/ac ${HytilsRebornConfig.autoGGMessage}") },
            HytilsRebornConfig.autoGGFirstMsgDelay.toLong(), TimeUnit.SECONDS
        )
        if (HytilsRebornConfig.autoGGSendSecondMessage) {
            Multithreading.schedule(
                { if (shouldSend) OmniClientChatSender.send("/ac ${HytilsRebornConfig.autoGGSecondMessage}") },
                (HytilsRebornConfig.autoGGFirstMsgDelay + HytilsRebornConfig.autoGGSecondMsgDelay).toLong(),
                TimeUnit.SECONDS
            )
        }

        Multithreading.schedule(
            { gameEnded = false; shouldSend = false },
            (HytilsRebornConfig.autoGGFirstMsgDelay + HytilsRebornConfig.autoGGSecondMsgDelay + 5).toLong(),
            TimeUnit.SECONDS
        )
    }

    @Subscribe
    fun onWorldUnload(event: WorldEvent.Unload) {
        gameEnded = false
        shouldSend = false
    }

    private fun hasGameEnded(message: String): Boolean {
        if (!gameEnded && message.matches(LanguageData.GAME_END)) {
            gameEnded = true
            return true
        }

        return HytilsRebornConfig.casualAutoGG && LanguageData.CASUAL_GAME_END.matches(message)
    }

    override fun isEnabled() = HytilsRebornConfig.autoGG
    // this should be one of the first modules to run
    override fun getPriority() = -3
}
