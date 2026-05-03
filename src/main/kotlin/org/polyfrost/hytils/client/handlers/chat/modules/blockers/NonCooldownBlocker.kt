package org.polyfrost.hytils.client.handlers.chat.modules.blockers

import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.HoverEvent
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.hypixel.data.rank.PackageRank
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatSendEvent
import org.polyfrost.hytils.client.handlers.chat.ChatSendModule
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.text.DecimalFormat

object NonCooldownBlocker : ChatSendModule {
    private var lastSent = 0L
    private val decimalFormat = DecimalFormat("#.#")

    private const val COOLDOWN_SECONDS = 3L

    override fun onChatSend(event: ChatSendEvent) {
        if (event.message.startsWith("/")) return

        val rank = HypixelUtils.getPlayerInfo().packageRank
        if (rank.isPresent && rank.get() != PackageRank.NONE) return

        if (lastSent < System.currentTimeMillis()) {
            lastSent = System.currentTimeMillis() + (COOLDOWN_SECONDS * 1000L)
        } else {
            val secondsLeft = (lastSent - System.currentTimeMillis()) / 1000L
            OmniClientChat.displayChatMessage(
                Text.literal(
                    "Your freedom of speech is on cooldown. Please wait ${decimalFormat.format(secondsLeft)} more second${if (secondsLeft == 1L) "" else "s"} before sending another message.",
                    MCTextStyle
                        .color(TextColors.YELLOW)
                        .setHoverEvent(HoverEvent.ShowText(
                            Text.literal("Hytils Reborn\n", MCTextStyle.color(TextColors.GOLD).setBold(true))
                                .append(Text.literal("Your message was blocked by the \"Non Speech Cooldown\" setting. \nPlease wait before sending another message.", MCTextStyle.color(TextColors.GRAY).setBold(false))))
                        )
                )
            )
            event.cancelled = true
        }
    }

    override fun isEnabled() = HytilsRebornConfig.preventNonCooldown
    override fun getPriority() = -1
}
