package org.polyfrost.hytils.client.handlers.game.titles

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.Locale

object GameStartingTitles {
    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (!HypixelUtils.isHypixel() || !HytilsRebornConfig.hideGameStartingTitles) return

        when (event.unformattedTitle.uppercase(Locale.ROOT)) {
            "WAITING FOR MORE PLAYERS...",
            "SKYWARS",
            "INSANE MODE",
            "FIGHT!",
            "ZOMBIES",
            "ASSASSINS",
            "YOU ARE RED",
            "YOU ARE BLUE",
            "YOU ARE YELLOW",
            "YOU ARE GREEN",
            "PRE ROUND",
            "ROUND START" ->
                event.cancelled = true
        }
    }
}
