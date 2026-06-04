package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.events.ChatSendEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.features.chat.handlers.ChatSendModule
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import java.text.DecimalFormat
import kotlin.jvm.optionals.getOrNull

object ShoutBlocker : ChatSendModule, ChatReceiveModule {
    var shoutCooldown = 0L
    val decimalFormat = DecimalFormat("#.#")

    override fun onChatSend(event: ChatSendEvent) {
        if (!event.message.startsWith("/shout ")) return

        if (shoutCooldown < System.currentTimeMillis()) {
            shoutCooldown = System.currentTimeMillis() + getCooldownLengthInSeconds() * 1000
        } else {
            val secondsLeft = (shoutCooldown - System.currentTimeMillis()) / 1000L
            mc.player?.displayClientMessage(
                Component.literal("Shout command is on cooldown. Please wait ${decimalFormat.format(secondsLeft)} more second${if (secondsLeft == 1L) "" else "s"} before shouting another message.")
                    .setStyle(Style.EMPTY.withHoverEvent(
                        HoverEvent.ShowText(
                            Component.empty()
                                .append(Component.literal("Hytils Reborn\n")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                                .append(Component.literal("Your message was blocked by the \"Shout Cooldown\" setting. \nPlease wait before shouting another message.")
                                    .withStyle(ChatFormatting.GRAY))
                        )
                    )),
                false
            )
            event.cancelled = true
        }
    }

    override fun onChatReceived(event: ChatReceiveEvent) {
        val location = HypixelUtils.getLocation()

        val message = event.plainMessage
        if ((location.gameType.getOrNull() == GameType.SKYWARS && message == LanguageData.CANNOT_SHOUT_BEFORE_SKYWARS)
            || message == LanguageData.CANNOT_SHOUT_BEFORE_GAME
            || message == LanguageData.CANNOT_SHOUT_AFTER_GAME
            || message == LanguageData.NO_SPECTATOR_COMMANDS
        ) {
            shoutCooldown = 0L
        }
    }

    private fun getCooldownLengthInSeconds(): Long {
        val location = HypixelUtils.getLocation()
        if ("LOBBY" != location.mode.getOrNull() && location.gameType.isPresent) {
            when (location.gameType.get()) {
                GameType.BEDWARS -> if ("BEDWARS_EIGHT_ONE" != location.mode.getOrNull()) return 60L
                GameType.SKYWARS -> return 3L
                GameType.ARCADE -> if ("PVP_CTW" == location.mode.orElse(null)) return 10L
                GameType.UHC -> if ("TEAMS" == location.mode.orElse(null)) return 90L
                else -> {}
            }
        }
        return 0L
    }

    override val isEnabled
        get() = HytilsRebornConfig.preventShoutingOnCooldown
    override val priority = -1
}
