package org.polyfrost.hytils.client.huds

import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.hud.v1.TextHud

class GameModeHud : TextHud(
    "game_mode.json",
    "Game Mode",
    Category.INFO,
    "Game Mode: "
) {
    @Switch(title = "Hide If Not In-Game or Supported")
    var shouldHide = true

    @Text(title = "No Location Text")
    var noLocationText = "Unknown"

    private var currentText = noLocationText

    override fun getText() = currentText

    override fun defaultPosition() = 0f to 0f

    override fun setup() {
        super.setup()

        eventHandler { event: HypixelLocationEvent ->
            val text = event.location.mode.orElse(null)
            // convert from screaming snake case to title case
            currentText = text?.lowercase()
                ?.split('_')
                ?.joinToString(" ") { word ->
                    word.replaceFirstChar { it.titlecase() }
                } ?: noLocationText
            hidden = text == null && shouldHide
            updateAndRecalculate()
        }

        if (isReal) {
            updateWhenChanged("shouldHide")
        }
    }
}
