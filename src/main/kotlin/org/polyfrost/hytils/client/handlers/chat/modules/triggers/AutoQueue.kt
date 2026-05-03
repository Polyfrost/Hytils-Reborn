package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import dev.deftu.omnicore.api.world.OmniDimension
import dev.deftu.omnicore.api.world.dimensionType
import net.minecraft.client.multiplayer.ClientLevel
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.data.providers.GameIdentifiersData
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.oneconfig.api.event.v1.events.KeyInputEvent
import org.polyfrost.oneconfig.api.event.v1.events.MouseInputEvent
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoQueue : ChatReceiveModule {
    private var command: String? = null
    private var sentCommand = false
    private var gameEnded = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay || !hasGameEnded(event.unformattedMessage)) return

        val location = HypixelUtils.getLocation()
        if (location.gameType.isPresent && location.mode.isPresent) {
            val databaseName = location.gameType.get().databaseName
            val gameMode = location.mode.get()
            val identifier = GameIdentifiersData.games[databaseName]?.get(gameMode) ?: gameMode
            command = "/play $identifier"
        }
    }

    @Subscribe
    fun onKeyInput(event: KeyInputEvent) {
        if (HytilsRebornConfig.isEnabled && command != null) {
            queue()
        }
    }

    // TODO: mouse input doesn't seem to work
    @Subscribe
    fun onMouseInput(event: MouseInputEvent) {
        if (HytilsRebornConfig.isEnabled && command != null) {
            queue()
        }
    }

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load) {
        gameEnded = false

        // stop the command from being spammed
        if (event.getWorld<ClientLevel>().dimensionType == OmniDimension.Overworld && sentCommand) {
            sentCommand = false
        }
    }

    private fun queue() {
        Multithreading.schedule({
            if (!sentCommand) {
                OmniClientChatSender.send(command!!) // FIXME: .queue
                sentCommand = true
                command = null
            }
        }, HytilsRebornConfig.autoQueueDelay.toLong(), TimeUnit.SECONDS)
    }

    private fun hasGameEnded(message: String): Boolean {
        if (gameEnded) return false

        val matched = message.matches(LanguageData.GAME_END)
        if (matched) {
            gameEnded = true
        }

        return matched
    }

    override fun isEnabled() = HytilsRebornConfig.autoQueue
    // this should run earlier than other modules but after autogg
    override fun getPriority() = -2
}
