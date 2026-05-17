package org.polyfrost.hytils.client.features.game

import net.hypixel.data.type.GameType
import net.minecraft.world.item.equipment.Equippable
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object HideArmor {
    @JvmStatic
    fun shouldHideArmor(equippable: Equippable): Boolean {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.hideArmor || equippable.assetId().isEmpty || !HypixelModAPIImpl.onHypixel) return false

        val location = HypixelUtils.getLocation()
        val gameType = location.gameType.orElse(null) ?: return false
        val mode = location.mode.orElse(null) ?: return false

        //~ if <1.21.11 '.identifier()' -> '.location()'
        val assetPath = equippable.assetId().get().identifier().path

        return if (assetPath.startsWith("leather")) {
            when (gameType) {
                GameType.BEDWARS -> !mode.contains("LUCKY")
                GameType.ARCADE -> mode.contains("PVP_CTW")
                GameType.DUELS -> mode.contains("BRIDGE") || mode.contains("CAPTURE") || mode.contains("ARENA")
                else -> false
            }
        } else {
            gameType == GameType.DUELS && (mode.contains("CLASSIC") || mode.contains("UHC"))
        }
    }
}
