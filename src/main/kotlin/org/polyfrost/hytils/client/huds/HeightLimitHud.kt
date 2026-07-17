package org.polyfrost.hytils.client.huds

import org.polyfrost.hytils.client.data.providers.HeightLimitData
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.hud.v1.TextHud
import org.polyfrost.oneconfig.utils.v1.dsl.mc

class HeightLimitHud : TextHud(
    "height_limit.json",
    "Height Limit",
    Category.INFO,
    "Height Limit: "
) {
    @Switch(title = "Show Distance To Limit")
    var showDistanceToLimit = false

    @Switch(title = "Use Minimum Build Limit")
    var useMinimumBuildLimit = false

    @Switch(title = "Hide If Not In-Game or Supported")
    var shouldHide = true

    @Text(title = "No Location Text")
    var noLocationText = "Unknown"

    private var buildLimit: Int? = 0

    override fun getText(): String = buildLimit?.let {
        if (showDistanceToLimit) it - (mc.player?.y?.toInt() ?: 0) else it
    }?.toString() ?: noLocationText

    override fun defaultPosition() = 0f to 0f

    override fun setup() {
        super.setup()

        eventHandler { event: HypixelLocationEvent ->
            val mapEntry = HeightLimitData.maps[event.location.gameType.orElse(null)]
                ?.get(event.location.mapName.orElse(null))
            buildLimit = if (useMinimumBuildLimit) mapEntry?.minBuild else mapEntry?.maxBuild
            hidden = buildLimit == null && shouldHide
            updateAndRecalculate()
        }

        if (isReal) {
            updateWhenChanged("shouldHide")
        }
    }
}
