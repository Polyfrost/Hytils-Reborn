package org.polyfrost.hytils.client.events

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
//? if >=1.21.11 {
import net.minecraft.client.renderer.SubmitNodeCollector
//~ if <26.1 'level.CameraRenderState' -> 'CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState
//?} else
//import net.minecraft.client.Camera
import org.polyfrost.oneconfig.api.event.v1.events.Event

data class PostLevelRenderEvent(
    val poseStack: PoseStack,
    /*? if >=1.21.11 {*/ val submitNodeCollector: SubmitNodeCollector, /*?}*/
    val multiBufferSource: MultiBufferSource,
    //~ if <1.21.11 'CameraRenderState' -> 'Camera'
    val camera: CameraRenderState
) : Event
