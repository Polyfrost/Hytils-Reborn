package org.polyfrost.hytils.client.handlers.game

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.SoundPlayEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object DropperHurtSound {
    @Subscribe
    fun onSoundPlay(event: SoundPlayEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.muteDropperHurtSound) return

        val location = HypixelUtils.getLocation()
        val path = event.sound.identifier.path
        if (HypixelUtils.isHypixel() && "dropper".equals(location.mode.orElse(null), true) && path == "entity.player.hurt") {
            event.cancelled = true
        }
    }
}
