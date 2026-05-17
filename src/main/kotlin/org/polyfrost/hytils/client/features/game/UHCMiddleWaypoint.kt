package org.polyfrost.hytils.client.features.game

import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.PostLevelRenderEvent
import org.polyfrost.hytils.client.utils.RenderUtils
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.color.rgba
import kotlin.math.hypot
import kotlin.math.roundToInt

object UHCMiddleWaypoint {
    private val pos = Vec3(0.0, 70.0, 0.0) // y = 70 is completely arbitrary, just slightly above sea level

    @Subscribe
    fun onPostLevelRender(event: PostLevelRenderEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.uhcMiddleWaypoint) return

        val location = HypixelUtils.getLocation()
        if (!HypixelUtils.isHypixel() || location.gameType.orElse(null) != GameType.UHC || location.gameType.orElse(null) != GameType.SPEED_UHC) return

        val distance = hypot(pos.x - mc.player!!.x, pos.z - mc.player!!.z)
        RenderUtils.renderText(
            Component.literal(HytilsRebornConfig.uhcMiddleWaypointText),
            Component.literal("${distance.roundToInt()}m").withStyle(ChatFormatting.YELLOW),
            pos = pos,
            camera = event.camera,
            color = PolyColor.WHITE,
            backgroundColor = rgba(0, 0, 0, 0.25f),
            disableDepth = true,
            dynamic = true
        )
    }
}
