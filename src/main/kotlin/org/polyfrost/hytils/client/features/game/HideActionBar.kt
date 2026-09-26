package org.polyfrost.hytils.client.features.game

//? if >=1.21.8 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
//?}

import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object HideActionBar {
    fun init() {
        //? if >=1.21.8 {
        HudElementRegistry.replaceElement(VanillaHudElements.OVERLAY_MESSAGE) { hudElement ->
            if (shouldHide()) { _, _ ->  } else hudElement
        }
        //?}
    }

    @JvmStatic
    fun shouldHide(): Boolean {
        val location = HypixelUtils.getLocation()
        if (!HytilsRebornConfig.isEnabled || !HypixelUtils.isHypixel() || !location.inGame()) return false

        return (HytilsRebornConfig.hideHousingActionBar && location.gameType.orElse(null) == GameType.HOUSING)
            || (HytilsRebornConfig.hideDropperActionBar && location.mode.orElse("").contains("DROPPER"))
    }
}
