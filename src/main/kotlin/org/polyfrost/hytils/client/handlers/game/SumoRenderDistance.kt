package org.polyfrost.hytils.client.handlers.game

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.event.v1.events.PostWorldRenderEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object SumoRenderDistance {
    var renderDistance = -1
    var wasSumo = false
    var changedRenderDistance = false

    @Subscribe
    fun onWorldRender(event: PostWorldRenderEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.sumoRenderDistance) return

        val location = HypixelUtils.getLocation()
        if (location.mode.orElse("").contains("SUMO")) {
            if (!changedRenderDistance) {
                renderDistance = mc.options.renderDistance().get()
                mc.options.renderDistance().set(HytilsRebornConfig.sumoRenderDistanceAmount.toInt())
                wasSumo = true
                changedRenderDistance = true
            }
        } else if (wasSumo) {
            mc.options.renderDistance().set(renderDistance)
            wasSumo = false
            changedRenderDistance = false
        }
    }
}
