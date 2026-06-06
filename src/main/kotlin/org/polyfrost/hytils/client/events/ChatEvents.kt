package org.polyfrost.hytils.client.events

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.data.providers.LanguageData.removeFormattingCodes
import org.polyfrost.oneconfig.api.event.v1.events.Event

data class ChatSendEvent(var message: String) : Event.Cancellable()

data class ChatReceiveEvent(var message: Component, var isOverlay: Boolean) : Event.Cancellable() {
    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    var cancelled: Boolean
        get() = super.cancelled && !HytilsRebornConfig.cancelledMessageDebugger
        set(value) {
            if (HytilsRebornConfig.cancelledMessageDebugger) {
                val caller = StackWalker.getInstance().walk { frames ->
                    frames.skip(1).findFirst().map { it.className.substringAfterLast('.') }.orElse("Unknown")
                }

                val originalMessage = message
                val hoverMessage = HoverEvent.ShowText(
                    Component.empty()
                        .append(Component.literal("Hytils Reborn\n")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                        .append(Component.literal("This message would have been cancelled. \nCancelled by: ")
                            .withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(caller).withStyle(ChatFormatting.YELLOW))
                )

                message = originalMessage.plainCopy()
                    .withStyle(originalMessage.style.withStrikethrough(true).withHoverEvent(hoverMessage))

                originalMessage.siblings.forEach {
                    (message as MutableComponent).append(
                        Component.literal(LanguageData.FORMATTING_CODES.replace(it.string) { code -> "${code.value}§m" })
                            .withStyle(it.style.withStrikethrough(true).withHoverEvent(hoverMessage)))
                }
            }
            super.cancelled = value
        }

    val plainMessage: String
        get() = this.message.string

    val unformattedMessage: String
        get() = this.plainMessage.removeFormattingCodes()
}
