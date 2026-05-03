package org.polyfrost.hytils.client.events

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.state.LevelRenderState
import org.polyfrost.oneconfig.api.event.v1.events.Event

data class PostLevelRenderEvent(
    val poseStack: PoseStack,
    val submitNodeCollector: SubmitNodeCollector,
    val multiBufferSource: MultiBufferSource,
    val levelRenderState: LevelRenderState
) : Event
