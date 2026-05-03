package org.polyfrost.hytils.client.handlers.game

import net.hypixel.data.type.GameType
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.event.v1.events.RenderLivingEvent
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object PitLagReducer {
    private var pitSpawnPos = -1.0

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load) {
        pitSpawnPos = -1.0
    }

    @Subscribe
    fun onRenderLiving(event: RenderLivingEvent.Pre) {
        if (!HytilsRebornConfig.isEnabled || !HypixelUtils.isHypixel()) return

        val location = HypixelUtils.getLocation()
        if (location.gameType.orElse(null) != GameType.PIT) return

        val entity: EntityRenderState = event.entity as EntityRenderState
        if (entity !is LivingEntityRenderState) return

        if (pitSpawnPos == -1.0) {
            // Update the spawn position by finding an armor stand in spawn.
            if (entity is ArmorStandRenderState && entity.nameTag?.string.equals("JUMP! FIGHT!")) {
                pitSpawnPos = entity.y - 5
            }
        } else if (HytilsRebornConfig.pitLagReducer) {
            // If the entity being rendered is at spawn, and you are below spawn, cancel the rendering.
            if (entity.y > pitSpawnPos && mc.player?.y!! < pitSpawnPos) {
                event.cancelled = true
            }
        }
    }
}
