package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoWB : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val match = LanguageData.PLAYER_CONNECTION_STATUS.find(event.unformattedMessage) ?: return

        val status = match.groups["status"]?.value ?: return
        if (status != "joined") return

        val player = match.groups["player"]?.value ?: return
        val type = match.groups["type"]?.value ?: return

        val command = when (type) {
            "Guild" -> if (HytilsRebornConfig.guildAutoWB) "/gc" else return
            "Friend" -> if (HytilsRebornConfig.friendsAutoWB) "/msg $player" else return
            else -> return
        }
        val message = if (HytilsRebornConfig.randomAutoWB) {
            getMessage(player)
        } else {
            HytilsRebornConfig.autoWBMessage1.replace("%player%", player)
        }

        Multithreading.schedule(
            { ChatUtils.sendMessage("$command $message") },
            HytilsRebornConfig.autoWBCooldown.toLong(),
            TimeUnit.SECONDS
        )
    }

    private fun getMessage(name: String): String {
        val validMessages = HytilsRebornConfig.wbMessages.filter { it.isNotBlank() }

        val message = if (validMessages.isEmpty()) {
            "Welcome Back!"
        } else {
            validMessages.random()
        }

        return message.replace("%player%", name)
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoWB
}
