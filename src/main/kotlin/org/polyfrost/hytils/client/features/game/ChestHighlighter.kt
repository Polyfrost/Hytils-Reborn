package org.polyfrost.hytils.client.features.game

import net.hypixel.data.type.GameType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.PostLevelRenderEvent
import org.polyfrost.hytils.client.events.TitleEvent
import org.polyfrost.hytils.client.utils.RenderUtils
import org.polyfrost.oneconfig.api.event.v1.events.PlayerInteractEvent
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object ChestHighlighter {
    private val highlightedChestPositions = mutableSetOf<BlockPos>()

    @Subscribe
    fun onPostLevelRender(event: PostLevelRenderEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.highlightChests || highlightedChestPositions.isEmpty() || !shouldHighlight()) return

        for (pos in highlightedChestPositions) {
            if (mc.level?.getBlockEntity(pos) !is ChestBlockEntity) continue
            RenderUtils.renderFilledBox(
                event.poseStack,
                Vec3(pos),
                //~ if <1.21.10 '.pos' -> '.position'
                event.camera.pos,
                HytilsRebornConfig.highlightChestsColor
            )
        }
    }

    @Subscribe
    fun onInteract(event: PlayerInteractEvent) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.highlightChests || !shouldHighlight()) return

        val hitResult = mc.hitResult as? BlockHitResult ?: return
        val blockEntity = mc.level?.getBlockEntity(hitResult.blockPos) ?: return
        if (blockEntity !is ChestBlockEntity) return

        when (event.action) {
            PlayerInteractEvent.Action.RIGHT -> highlightedChestPositions.add(blockEntity.blockPos)
            PlayerInteractEvent.Action.LEFT -> highlightedChestPositions.remove(blockEntity.blockPos)
        }
    }

    @Subscribe
    fun onTitle(event: TitleEvent) {
        if (event.unformattedSubtitle == "All chests have been refilled!") {
            highlightedChestPositions.clear()
        }
    }

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load) {
        highlightedChestPositions.clear()
    }

    private fun shouldHighlight(): Boolean {
        val location = HypixelUtils.getLocation()
        val gameType = location.gameType.orElse(null)
        return gameType == GameType.SKYWARS
                || gameType == GameType.SURVIVAL_GAMES
                || gameType == GameType.UHC
                || (gameType == GameType.DUELS && location.mode.orElse("").contains("_SW_"))
    }
}
