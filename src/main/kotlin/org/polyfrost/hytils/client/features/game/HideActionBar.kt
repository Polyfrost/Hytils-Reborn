package org.polyfrost.hytils.client.features.game

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object HideActionBar {
    fun init() {
        HudElementRegistry.replaceElement(VanillaHudElements.OVERLAY_MESSAGE) { hudElement ->
            val location = HypixelUtils.getLocation()
            if (!HytilsRebornConfig.isEnabled || !HypixelUtils.isHypixel() || !location.inGame())
                return@replaceElement hudElement

            if ((HytilsRebornConfig.hideHousingActionBar && location.gameType.orElse(null) == GameType.HOUSING)
                || (HytilsRebornConfig.hideDropperActionBar && location.mode.orElse("").contains("DROPPER")))
                return@replaceElement { _, _ ->  }

            return@replaceElement hudElement
        }
    }
}
