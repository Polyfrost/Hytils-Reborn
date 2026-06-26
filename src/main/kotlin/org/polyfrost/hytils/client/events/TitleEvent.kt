package org.polyfrost.hytils.client.events

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.oneconfig.api.event.v1.events.Event

data class TitleEvent(val title: Component, val subtitle: Component?) : Event.Cancellable() {
    val plainTitle: String
        get() = this.title.string

    val unformattedTitle: String
        get() = ChatFormatting.stripFormatting(this.plainTitle)!!

    val plainSubtitle: String?
        get() = this.subtitle?.string

    val unformattedSubtitle: String?
        get() = ChatFormatting.stripFormatting(this.plainSubtitle)
}
