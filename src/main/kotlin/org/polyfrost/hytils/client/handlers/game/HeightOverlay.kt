package org.polyfrost.hytils.client.handlers.game

import net.hypixel.data.type.GameType
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.HeightLimitData
import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

// TODO: add skywars support (already supported in the api, but we only recolor wool & terracotta)
object HeightOverlay {
    private val ALLOWED_TERRACOTTA_COLORS = setOf(
        MapColor.TERRACOTTA_RED, MapColor.TERRACOTTA_BLUE, MapColor.TERRACOTTA_GREEN, MapColor.TERRACOTTA_YELLOW
    )

    private var maxBuild: Int? = null
    private var minBuild: Int? = null

    @JvmStatic
    fun shouldModify(blockState: BlockState, blockPos: BlockPos): Boolean {
        if (!HypixelModAPIImpl.onHypixel || !canColor(blockState)) return false

        return blockPos.y == maxBuild?.minus(1)
                || (HytilsRebornConfig.heightOverlayMinBuild && blockPos.y == minBuild?.plus(1))
    }

    @JvmStatic
    fun modifyColors(color: Int, blockState: BlockState): Int {
        val multiplier = (1000f - HytilsRebornConfig.overlayAmount) / 1000f
        val r: Float
        val g: Float
        val b: Float

        if (!HytilsRebornConfig.HeightOverlayCustomColors.enabled) {
            r = ((color and 0xFF) * multiplier).coerceIn(0f, 255f)
            g = (((color shr 8) and 0xFF) * multiplier).coerceIn(0f, 255f)
            b = (((color shr 16) and 0xFF) * multiplier).coerceIn(0f, 255f)
        } else {
            val customColor = HytilsRebornConfig.HeightOverlayCustomColors.getColor(blockState.block.defaultMapColor())
            r = (customColor.r * multiplier).coerceIn(0f, 255f)
            g = (customColor.g * multiplier).coerceIn(0f, 255f)
            b = (customColor.b * multiplier).coerceIn(0f, 255f)
        }

        return (0xFF shl 24) or
                (b.toInt() shl 16) or
                (g.toInt() shl 8) or
                r.toInt()
    }

    @Subscribe
    fun onLocationUpdate(event: HypixelLocationEvent) {
        maxBuild = getMaxBuildLimit()
        minBuild = getMinBuildLimit()
    }

    private fun getMaxBuildLimit(): Int? {
        val location = HypixelUtils.getLocation()
        if (!location.inGame() || HeightLimitData.maps.isEmpty()) return null

        return if (location.gameType.isPresent) {
            when (location.gameType.get()) {
                GameType.BEDWARS ->
                    HeightLimitData.maps[location.gameType.get()]?.get(location.mapName.orElse(""))?.maxBuild
                GameType.DUELS -> if (location.mode.orElse("").contains("BRIDGE")) 100 else null
                else -> null
            }
        } else {
            null
        }
    }

    private fun getMinBuildLimit(): Int? {
        val location = HypixelUtils.getLocation()
        if (!location.inGame() || HeightLimitData.maps.isEmpty()) return null

        return if (location.gameType.isPresent) {
            when (location.gameType.get()) {
                GameType.BEDWARS ->
                    HeightLimitData.maps[location.gameType.get()]?.get(location.mapName.orElse(""))?.minBuild
                GameType.DUELS -> if (location.mode.orElse("").contains("BRIDGE")) 83 else null
                else -> null
            }
        } else {
            null
        }
    }

    private fun canColor(blockState: BlockState): Boolean {
        return blockState.`is`(BlockTags.WOOL)
                || (blockState.`is`(BlockTags.TERRACOTTA) && ALLOWED_TERRACOTTA_COLORS.contains(blockState.block.defaultMapColor()))
    }
}
