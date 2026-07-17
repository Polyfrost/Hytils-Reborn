package org.polyfrost.hytils.client.huds

import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.hud.v1.TextHud

class GameTypeHud : TextHud(
    "game_type.json",
    "Game Type",
    Category.INFO,
    "Game Type: "
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

        @Suppress("UsePropertyAccessSyntax")
        eventHandler { event: HypixelLocationEvent ->
            val text = event.location.gameType.orElse(null)?.getName()
            currentText = text ?: noLocationText
            hidden = text == null && shouldHide
            updateAndRecalculate()
        }

        if (isReal) {
            updateWhenChanged("shouldHide")
        }
    }
}
