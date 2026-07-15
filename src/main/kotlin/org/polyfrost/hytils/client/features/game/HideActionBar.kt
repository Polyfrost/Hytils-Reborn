package org.polyfrost.hytils.client.features.game

//~ if <1.21.8 'hud.HudElementRegistry' -> 'HudLayerRegistrationCallback'
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
//~ if <1.21.8 'hud.VanillaHudElements' -> 'IdentifiedLayer'
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object HideActionBar {
    fun init() {
        //? if >=1.21.8 {
        HudElementRegistry.replaceElement(VanillaHudElements.OVERLAY_MESSAGE) { hudElement ->
        //?} else
        //HudLayerRegistrationCallback.EVENT.register { layers ->
            val location = HypixelUtils.getLocation()
            if (!HytilsRebornConfig.isEnabled || !HypixelUtils.isHypixel() || !location.inGame())
                //~ if <1.21.8 '@replaceElement hudElement' -> '@register'
                return@replaceElement hudElement

            if ((HytilsRebornConfig.hideHousingActionBar && location.gameType.orElse(null) == GameType.HOUSING)
                || (HytilsRebornConfig.hideDropperActionBar && location.mode.orElse("").contains("DROPPER")))
                //? if >=1.21.8 {
                return@replaceElement { _, _ ->  }
                //?} else
                //layers.removeLayer(IdentifiedLayer.OVERLAY_MESSAGE)

            //~ if <1.21.8 '@replaceElement hudElement' -> '@register'
            return@replaceElement hudElement
        }
    }
}
