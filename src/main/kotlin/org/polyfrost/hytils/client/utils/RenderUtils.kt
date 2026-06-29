package org.polyfrost.hytils.client.utils

//? if >=26.1 {
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.platform.CompareOp
//?}

//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderTypes
import org.polyfrost.hytils.mixin.client.accessor.RenderTypeAccessor
//?} else
//import net.minecraft.client.renderer.RenderType

//? if >=1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector
//~ if <26.1 'level.CameraRenderState' -> 'CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState
//?}

//? if >=1.21.5 {
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.renderer.RenderPipelines
//?}

import com.mojang.blaze3d.vertex.*
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
//~ if <26.1 'util.LightCoordsUtil' -> 'client.renderer.LightTexture as LightCoordsUtil'
import net.minecraft.util.LightCoordsUtil
//~ if <1.21.11 'util.Util' -> 'Util'
import net.minecraft.util.Util
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import kotlin.math.pow
import kotlin.math.sqrt

object RenderUtils {
    private const val Z_FIGHTING_OFFSET = 0.001f

    //? if >=1.21.5 {
    private val BEACON_BEAM_OPAQUE_NO_DEPTH = RenderPipeline.builder(RenderPipelines.BEACON_BEAM_SNIPPET)
        .withLocation("pipeline/beacon_beam_opaque")
        //? if >=26.1 {
        .withDepthStencilState(java.util.Optional.empty())
        //?} else
        //.withDepthTestFunction(com.mojang.blaze3d.platform.DepthTestFunction.NO_DEPTH_TEST)
        .build()
    private val BEACON_BEAM_TRANSLUCENT_NO_DEPTH = RenderPipeline.builder(RenderPipelines.BEACON_BEAM_SNIPPET)
        .withLocation("pipeline/beacon_beam_translucent")
        //? if >=26.1 {
        .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
        .withDepthStencilState(java.util.Optional.empty())
        //?} else {
        /*.withDepthWrite(false)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withDepthTestFunction(com.mojang.blaze3d.platform.DepthTestFunction.NO_DEPTH_TEST)
        *///?}
        .build()
    //?}

    @JvmField
    //? if >=1.21.11 {
    val BEACON_BEAM_NO_DEPTH = Util.memoize { identifier: Identifier, isTranslucent: Boolean ->
        val pipeline = if (isTranslucent) {
            BEACON_BEAM_TRANSLUCENT_NO_DEPTH
        } else {
            BEACON_BEAM_OPAQUE_NO_DEPTH
        }

        val state = RenderSetup.builder(pipeline)
            .withTexture("Sampler0", identifier)
            .sortOnUpload()
            .createRenderSetup()

        RenderTypeAccessor.invokeCreate("beacon_beam", state)
    }
    //?} else {
    /*val BEACON_BEAM_NO_DEPTH: java.util.function.BiFunction<Identifier, Boolean, RenderType> = Util.memoize { resourceLocation, isTranslucent ->
        val compositeState = RenderType.CompositeState.builder()
            //? if <1.21.5 {
            /*.setShaderState(net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_BEACON_BEAM_SHADER)
            .setTransparencyState(if (isTranslucent) net.minecraft.client.renderer.RenderStateShard.TRANSLUCENT_TRANSPARENCY else net.minecraft.client.renderer.RenderStateShard.NO_TRANSPARENCY)
            .setWriteMaskState(if (isTranslucent) net.minecraft.client.renderer.RenderStateShard.COLOR_WRITE else net.minecraft.client.renderer.RenderStateShard.COLOR_DEPTH_WRITE)
            .setDepthTestState(net.minecraft.client.renderer.RenderStateShard.NO_DEPTH_TEST)
            *///?}
            //~ if <1.21.8 'resourceLocation, false' -> 'resourceLocation, net.minecraft.util.TriState.FALSE, false'
            .setTextureState(net.minecraft.client.renderer.RenderStateShard.TextureStateShard(resourceLocation, false))
            .createCompositeState(false)
        //? if >=1.21.5 {
        val pipeline = if (isTranslucent) {
            BEACON_BEAM_TRANSLUCENT_NO_DEPTH
        } else {
            BEACON_BEAM_OPAQUE_NO_DEPTH
        }
        //?}

        RenderType.create(
            "beacon_beam",
            //? if <1.21.5 {
            /*DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            *///?}
            1536,
            false,
            true,
            //? if >=1.21.5
            pipeline,
            compositeState
        )
    }
    *///?}
    @JvmField
    var beaconDisableDepth = false

    // FIXME: not 'interpolated' correctly on 26.2
    fun renderFilledBox(
        poseStack: PoseStack,
        //? if >=26.2 {
        submitNodeCollector: SubmitNodeCollector,
        //?} else
        //multiBufferSource: net.minecraft.client.renderer.MultiBufferSource,
        pos: Vec3,
        cameraPos: Vec3,
        color: PolyColor,
        alpha: Float = 0.8f
    ) {
        //? if >=26.2 {
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads()) { _, vertexConsumer ->
        //?} else {
        /*//~ if <1.21.11 'RenderTypes' -> 'RenderType'
        multiBufferSource.getBuffer(RenderTypes.debugQuads()).also { vertexConsumer ->
        *///?}
            poseStack.pushPose()
            poseStack.translate(
                pos.x - cameraPos.x,
                pos.y - cameraPos.y,
                pos.z - cameraPos.z
            )

            addBox(
                poseStack.last().pose(), vertexConsumer, color.withAlpha(alpha).argb,
                0f - Z_FIGHTING_OFFSET, 0f - Z_FIGHTING_OFFSET, 0f - Z_FIGHTING_OFFSET,
                1f + Z_FIGHTING_OFFSET, 1f + Z_FIGHTING_OFFSET, 1f + Z_FIGHTING_OFFSET
            )

            poseStack.popPose()
        }
    }

    fun renderText(
        text: Component,
        pos: Vec3,
        //~ if <1.21.10 'CameraRenderState' -> 'net.minecraft.client.Camera'
        camera: CameraRenderState,
        color: PolyColor,
        backgroundColor: PolyColor,
        disableDepth: Boolean = false,
        dynamic: Boolean = false,
    ) {
        renderText(
            text,
            pos = pos,
            camera = camera,
            color = color,
            backgroundColor = backgroundColor,
            disableDepth = disableDepth,
            dynamic = dynamic
        )
    }

    fun renderText(
        vararg texts: Component,
        pos: Vec3,
        //? if >=26.2 {
        submitNodeCollector: SubmitNodeCollector,
        poseStack: PoseStack,
        //?}
        //~ if <1.21.10 'CameraRenderState' -> 'net.minecraft.client.Camera'
        camera: CameraRenderState,
        color: PolyColor,
        backgroundColor: PolyColor,
        disableDepth: Boolean = false,
        dynamic: Boolean = false,
    ) {
        //~ if <1.21.10 'camera.pos' -> 'camera.position' {
        var pos = pos
        var scale = 1f

        if (dynamic) {
            val eyeHeight = mc.player!!.getEyeHeight(mc.player!!.pose)

            val dX = (pos.x - camera.pos.x).pow(2)
            val dY = (pos.y - camera.pos.y + eyeHeight).pow(2)
            val dZ = (pos.z - camera.pos.z).pow(2)

            val distToPlayer = sqrt(dX + dY + dZ)
            val distRender = distToPlayer.coerceAtMost(50.0)
            scale = (distRender / 12).coerceAtLeast(0.5).toFloat()

            val resultX = camera.pos.x + (pos.x + 0.5 - camera.pos.x) / (distToPlayer / distRender)
            val resultY = camera.pos.y + eyeHeight + (pos.y + 20 * distToPlayer / 300 - (camera.pos.y + eyeHeight)) / (distToPlayer / distRender)
            val resultZ = camera.pos.z + (pos.z + 0.5 - camera.pos.z) / (distToPlayer / distRender)

            pos = Vec3(resultX, resultY, resultZ)
        }

        scale *= 0.05f

        //? if >=26.2 {
        poseStack.pushPose()

        poseStack.run {
        //?} else
        //val matrix = org.joml.Matrix4f().apply {
            translate(
                (pos.x - camera.pos.x).toFloat(),
                (pos.y - camera.pos.y).toFloat(),
                (pos.z - camera.pos.z).toFloat()
            )
            //~ if <26.2 'mulPose' -> 'rotate'
            //~ if <1.21.10 '.orientation' -> '.rotation()'
            mulPose(camera.orientation)
            scale(scale, -scale, scale)
        }
        //~}

        for (text in texts) {
            //? if >=26.2 {
            submitNodeCollector.submitText(
                poseStack,
                -mc.font.width(text) / 2f,
                0f,
                text.visualOrderText,
                false,
                if (disableDepth) Font.DisplayMode.SEE_THROUGH else Font.DisplayMode.NORMAL,
                LightCoordsUtil.FULL_BRIGHT,
                color.argb,
                backgroundColor.argb,
                0
            )
            //?} else {
            /*mc.font.drawInBatch(
                text,
                -mc.font.width(text) / 2f,
                0f,
                color.argb,
                false,
                matrix,
                mc.renderBuffers().bufferSource(),
                if (disableDepth) Font.DisplayMode.SEE_THROUGH else Font.DisplayMode.NORMAL,
                backgroundColor.argb,
                LightCoordsUtil.FULL_BRIGHT
            )
            *///?}

            //~ if <26.2 'poseStack' -> 'matrix'
            poseStack.translate(0f, mc.font.lineHeight + 2f, 0f)
        }

        //? if >=26.2
        poseStack.popPose()
    }

    // FIXME: broken on 26.1 only ("works" if rendered in `LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN`)
    fun renderBeaconBeam(
        poseStack: PoseStack,
        //? if >=1.21.10 {
        submitNodeCollector: SubmitNodeCollector,
        //?} else
        //multiBufferSource: net.minecraft.client.renderer.MultiBufferSource,
        pos: Vec3,
        cameraPos: Vec3,
        color: PolyColor,
        disableDepth: Boolean = false
    ) {
        poseStack.pushPose()
        poseStack.translate(
            pos.x - cameraPos.x,
            pos.y - cameraPos.y,
            pos.z - cameraPos.z
        )

        if (disableDepth) beaconDisableDepth = true
        //? if >=1.21.10 {
        BeaconRenderer.submitBeaconBeam(
            poseStack,
            submitNodeCollector,
            BeaconRenderer.BEAM_LOCATION,
            1f,
            (mc.level!!.gameTime % 40) + mc.deltaTracker.getGameTimeDeltaPartialTick(false),
            0,
            BeaconRenderer.MAX_RENDER_Y,
            color.argb,
            BeaconRenderer.SOLID_BEAM_RADIUS,
            BeaconRenderer.BEAM_GLOW_RADIUS
        )
        //?} else {
        /*BeaconRenderer.renderBeaconBeam(
            poseStack,
            multiBufferSource,
            BeaconRenderer.BEAM_LOCATION,
            mc.deltaTracker.getGameTimeDeltaPartialTick(false),
            1f,
            mc.level!!.gameTime,
            0,
            BeaconRenderer.MAX_RENDER_Y,
            color.argb,
            //~ if <1.21.5 'BeaconRenderer.SOLID_BEAM_RADIUS' -> '0.2f'
            BeaconRenderer.SOLID_BEAM_RADIUS,
            //~ if <1.21.5 'BeaconRenderer.BEAM_GLOW_RADIUS' -> '0.25f'
            BeaconRenderer.BEAM_GLOW_RADIUS
        )
        *///?}
        if (disableDepth) beaconDisableDepth = false

        poseStack.popPose()
    }

    private fun addBox(
        positionMatrix: Matrix4f,
        vertexConsumer: VertexConsumer,
        color: Int,
        minX: Float, minY: Float, minZ: Float,
        maxX: Float, maxY: Float, maxZ: Float
    ) {
        // front face
        addQuad(
            vertexConsumer, positionMatrix, color,
            minX, minY, maxZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            minX, maxY, maxZ
        )

        // back face
        addQuad(
            vertexConsumer, positionMatrix, color,
            maxX, minY, minZ,
            minX, minY, minZ,
            minX, maxY, minZ,
            maxX, maxY, minZ
        )

        // left face
        addQuad(
            vertexConsumer, positionMatrix, color,
            minX, minY, minZ,
            minX, minY, maxZ,
            minX, maxY, maxZ,
            minX, maxY, minZ
        )

        // right face
        addQuad(
            vertexConsumer, positionMatrix, color,
            maxX, minY, maxZ,
            maxX, minY, minZ,
            maxX, maxY, minZ,
            maxX, maxY, maxZ
        )

        // top face
        addQuad(
            vertexConsumer, positionMatrix, color,
            minX, maxY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ,
            minX, maxY, minZ
        )

        // bottom face
        addQuad(
            vertexConsumer, positionMatrix, color,
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, minY, maxZ,
            minX, minY, maxZ
        )
    }

    private fun addQuad(
        vertexConsumer: VertexConsumer,
        positionMatrix: Matrix4f,
        color: Int,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        x4: Float, y4: Float, z4: Float
    ) {
        vertexConsumer.addVertex(positionMatrix, x1, y1, z1).setColor(color)
        vertexConsumer.addVertex(positionMatrix, x2, y2, z2).setColor(color)
        vertexConsumer.addVertex(positionMatrix, x3, y3, z3).setColor(color)
        vertexConsumer.addVertex(positionMatrix, x4, y4, z4).setColor(color)
    }
}
