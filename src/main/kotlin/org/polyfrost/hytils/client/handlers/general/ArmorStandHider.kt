package org.polyfrost.hytils.client.handlers.general

import net.hypixel.data.type.GameType
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.ArmorStandData
import org.polyfrost.hytils.client.data.providers.LanguageData.removeFormattingCodes
import org.polyfrost.oneconfig.api.event.v1.events.RenderLivingEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object ArmorStandHider {
    @Subscribe
    fun onEntityRender(event: RenderLivingEvent.Pre) {
        if (!HytilsRebornConfig.isEnabled || event.entity !is ArmorStandRenderState || !HypixelUtils.isHypixel() || !shouldHide()) return

        val name = (event.entity as ArmorStandRenderState).nameTag?.string?.removeFormattingCodes() ?: return
        if (ArmorStandData.armorStandNames.any { name.contains(it, ignoreCase = true) }) {
            event.cancelled = true
        }
    }

    private fun shouldHide(): Boolean {
        val location = HypixelUtils.getLocation()
        return when {
            HytilsRebornConfig.hideUselessArmorStands && !location.inGame() -> true
            HytilsRebornConfig.hideUselessArmorStandsGame && location.inGame() -> {
                location.gameType.orElse(null) in listOf(GameType.SKYBLOCK, GameType.BEDWARS, GameType.SKYWARS)
                        || location.mode.orElse("").contains("BRIDGE")
            }

            else -> false
        }
    }
}
