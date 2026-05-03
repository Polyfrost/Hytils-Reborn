package org.polyfrost.hytils.client.handlers.game.titles

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.Locale

object CountdownTitles {
    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (!HypixelUtils.isHypixel() || !HytilsRebornConfig.hideCountdownTitles) return

        when (event.unformattedTitle.uppercase(Locale.ROOT)) {
            "60",
            "30",
            "10",
            "9",
            "8",
            "7",
            "6",
            "5",
            "4",
            "3",
            "2",
            "1",
            "10 SECONDS",
            "❸",
            "❷",
            "❶" ->
                event.cancelled = true
        }
    }
}
