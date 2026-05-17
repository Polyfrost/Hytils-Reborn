package org.polyfrost.hytils.client.features.game.titles

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.Locale

object GameEndingTitles {
    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (!HypixelUtils.isHypixel() || !HytilsRebornConfig.hideGameEndingTitles) return

        when (event.unformattedTitle.uppercase(Locale.ROOT)) {
            "YOU WIN!",
            "YOU LOSE!",
            "VICTORY!",
            "GAME OVER!",
            "RESPAWNED!",
            "DEFEAT!",
            "GAME END",
            "YOU DIED",
            "DEFEAT",
            "GAME OVER" ->
                event.cancelled = true
        }
    }
}
