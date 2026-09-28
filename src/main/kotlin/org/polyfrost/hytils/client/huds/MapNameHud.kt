package org.polyfrost.hytils.client.huds

import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.hud.v1.TextHud

class MapNameHud : TextHud(
    "map_name.json",
    "Map Name",
    Category.INFO,
    "Map Name: "
) {
    @Switch(title = "Hide If Not In-Game or Supported")
    var shouldHide = true

    @Text(title = "No Location Text")
    var noLocationText = "Unknown"

    private var currentText = noLocationText
    private var available = false

    override fun getText() = currentText

    override fun defaultPosition() = 0f to 0f

    override fun shouldShow() = !shouldHide || available

    override fun setup() {
        super.setup()

        eventHandler { event: HypixelLocationEvent ->
            val text = event.location.mapName.orElse(null)
            currentText = text ?: noLocationText
            available = text != null
            updateAndRecalculate()
        }

        if (isReal) {
            updateWhenChanged("shouldHide")
        }
    }
}
