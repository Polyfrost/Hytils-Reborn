package org.polyfrost.hytils.client.events

import com.mojang.blaze3d.vertex.PoseStack
//? if >=1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector
//~ if <26.1 'level.CameraRenderState' -> 'CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState
//?}
import org.polyfrost.oneconfig.api.event.v1.events.Event

data class PostLevelRenderEvent(
    val poseStack: PoseStack,
    /*? if >=1.21.10 {*/ val submitNodeCollector: SubmitNodeCollector, /*?}*/
    //? if <26.2
    //val multiBufferSource: net.minecraft.client.renderer.MultiBufferSource,
    //~ if <1.21.10 'CameraRenderState' -> 'net.minecraft.client.Camera'
    val camera: CameraRenderState
) : Event
