package org.polyfrost.hytils.client.features.chat.handlers.modules.blockers

import net.hypixel.data.rank.PackageRank
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatSendEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatSendModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.text.DecimalFormat

object NonCooldownBlocker : ChatSendModule {
    private var lastSent = 0L
    private val decimalFormat = DecimalFormat("#.#")

    private const val COOLDOWN_SECONDS = 3L

    override fun onChatSend(event: ChatSendEvent) {
        if (event.message.startsWith("/")) return // FIXME: can be bypassed by `/ac` etc

        val rank = HypixelUtils.getPlayerInfo().packageRank
        if (rank.isPresent && rank.get() != PackageRank.NONE) return

        if (lastSent < System.currentTimeMillis()) {
            lastSent = System.currentTimeMillis() + (COOLDOWN_SECONDS * 1000L)
        } else {
            val secondsLeft = (lastSent - System.currentTimeMillis()) / 1000L
            ChatUtils.displayMessage(
                Component.literal("Your freedom of speech is on cooldown. Please wait ${decimalFormat.format(secondsLeft)} more second${if (secondsLeft == 1L) "" else "s"} before sending another message.")
                    .setStyle(Style.EMPTY.withHoverEvent(
                        //~ if <1.21.5 '.ShowText(' -> '(HoverEvent.Action.SHOW_TEXT,'
                        HoverEvent.ShowText(
                            Component.empty()
                                .append(Component.literal("Hytils Reborn\n")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                                .append(Component.literal("Your message was blocked by the \"Non Speech Cooldown\" setting. \nPlease wait before sending another message.")
                                    .withStyle(ChatFormatting.GRAY))
                        )
                    ))
            )
            event.cancelled = true
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.preventNonCooldown
    override val priority = -1
}
