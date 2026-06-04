package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoGG : ChatReceiveModule {
    private var gameEnded = false
    private var shouldSend = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (!hasGameEnded(event.unformattedMessage)) return

        shouldSend = true

        Multithreading.schedule(
            { if (shouldSend) ChatUtils.sendMessage("/ac ${HytilsRebornConfig.autoGGMessage}") },
            HytilsRebornConfig.autoGGFirstMsgDelay.toLong(), TimeUnit.SECONDS
        )
        if (HytilsRebornConfig.autoGGSendSecondMessage) {
            Multithreading.schedule(
                { if (shouldSend) ChatUtils.sendMessage("/ac ${HytilsRebornConfig.autoGGSecondMessage}") },
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

    override val isEnabled
        get() = HytilsRebornConfig.autoGG

    // this should be one of the first modules to run
    override val priority = -3
}
