package org.polyfrost.hytils.client.features.game

import net.minecraft.world.phys.Vec3
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.PostLevelRenderEvent
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.hytils.client.utils.RenderUtils
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object MiniWallsMiddleBeacon {
    var isMiniWitherDead = false

    @Subscribe
    fun onPostLevelRender(event: PostLevelRenderEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.miniWallsMiddleBeacon || !isMiniWitherDead) return

        val location = HypixelUtils.getLocation()
        if (!HypixelUtils.isHypixel() || location.mode.orElse(null) != "MINI_WALLS") return

        RenderUtils.renderBeaconBeam(
            event.poseStack,
            /*? if >=1.21.10 {*/ event.submitNodeCollector /*?} else {*/ /*event.multiBufferSource *//*?}*/,
            Vec3.ZERO,
            //~ if <1.21.10 '.pos' -> '.position'
            event.camera.pos,
            HytilsRebornConfig.miniWallsMiddleBeaconColor,
            true
        )
    }

    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (event.unformattedTitle == "Your Mini Wither died!" || event.unformattedTitle == "DEATHMATCH") {
            isMiniWitherDead = true
        }
    }

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load) {
        isMiniWitherDead = false
    }
}
