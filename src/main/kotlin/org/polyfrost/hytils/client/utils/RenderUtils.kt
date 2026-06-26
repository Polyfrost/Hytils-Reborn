package org.polyfrost.hytils.client.utils

//? if >=26.2 {
import com.mojang.blaze3d.PrimitiveTopology
import net.minecraft.client.renderer.StagedVertexBuffer
//?} else {
/*import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.vertex.MeshData.DrawState
import com.mojang.blaze3d.vertex.VertexFormat.IndexType
import net.minecraft.client.renderer.MappableRingBuffer

import org.lwjgl.system.MemoryUtil
*///?}

//? if >=26.1 {
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.platform.CompareOp
//?}

//? if >=1.21.11 {
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderSetup
//~ if <26.1 'level.CameraRenderState' -> 'CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState
import org.polyfrost.hytils.mixin.client.accessor.RenderTypeAccessor
//?}

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.blockentity.BeaconRenderer
//~ if <1.21.11 'rendertype.RenderType' -> 'RenderType'
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
//~ if <26.1 'util.LightCoordsUtil' -> 'client.renderer.LightTexture as LightCoordsUtil'
import net.minecraft.util.LightCoordsUtil
//~ if <1.21.11 'util.Util' -> 'Util'
import net.minecraft.util.Util
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes
import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Vector3f
import org.joml.Vector4f
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Some code was heavily adapted from Fabric Documentation's [Rendering in the World](https://docs.fabricmc.net/develop/rendering/world)
 * article ([`CustomRenderPipeline.java`](https://github.com/FabricMC/fabric-docs/blob/main/reference/latest/src/client/java/com/example/docs/rendering/CustomRenderPipeline.java)).
 */
object RenderUtils {
    //? if <26.2 {
    /*private val allocator: ByteBufferBuilder = ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE)
    private var buffer: BufferBuilder? = null
    *///?}

    private val COLOR_MODULATOR = Vector4f(1f, 1f, 1f, 1f)
    //? if >=1.21.11 {
    private val MODEL_OFFSET = Vector3f()
    private val TEXTURE_MATRIX = Matrix4f()
    //?}

    //? if <26.2 {
    /*private var vertexBuffer: MappableRingBuffer? = null
    *///?} else
    private val stagedBuffer = StagedVertexBuffer({ "${HytilsRebornConstants.NAME} Buffer" }, RenderType.SMALL_BUFFER_SIZE);

    private const val Z_FIGHTING_OFFSET = 0.001f

    private val BEACON_BEAM_OPAQUE_NO_DEPTH = RenderPipeline.builder(RenderPipelines.BEACON_BEAM_SNIPPET)
        .withLocation("pipeline/beacon_beam_opaque")
        //? if >=26.1 {
        .withDepthStencilState(Optional.empty())
        //?} else
        //.withDepthTestFunction(com.mojang.blaze3d.platform.DepthTestFunction.NO_DEPTH_TEST)
        .build()
    private val BEACON_BEAM_TRANSLUCENT_NO_DEPTH = RenderPipeline.builder(RenderPipelines.BEACON_BEAM_SNIPPET)
        .withLocation("pipeline/beacon_beam_translucent")
        //? if >=26.1 {
        .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
        .withDepthStencilState(Optional.empty())
        //?} else {
        /*.withDepthWrite(false)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withDepthTestFunction(com.mojang.blaze3d.platform.DepthTestFunction.NO_DEPTH_TEST)
        *///?}
        .build()

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
            .setTextureState(net.minecraft.client.renderer.RenderStateShard.TextureStateShard(resourceLocation, false))
            .createCompositeState(false)
        val pipeline = if (isTranslucent) {
            BEACON_BEAM_TRANSLUCENT_NO_DEPTH
        } else {
            BEACON_BEAM_OPAQUE_NO_DEPTH
        }
        
        RenderType.create(
            "beacon_beam",
            1536,
            false,
            true,
            pipeline,
            compositeState
        )
    }
    *///?}
    @JvmField
    var beaconDisableDepth = false

    fun renderFilledBox(poseStack: PoseStack, pos: Vec3, cameraPos: Vec3, color: PolyColor, alpha: Float = 0.8f) {
        //? if >=26.2 {
        val formatBinding = RenderPipelines.DEBUG_FILLED_BOX.getVertexFormatBinding(0) ?: return
        val primitive = RenderPipelines.DEBUG_FILLED_BOX.primitiveTopology
        val draw = stagedBuffer.appendDraw(
            formatBinding,
            primitive,
            if (primitive == PrimitiveTopology.QUADS) RenderSystem.getProjectionType().vertexSorting() else null
        )

        prepareFilledBox(poseStack, draw, pos, cameraPos, color, alpha)
        stagedBuffer.upload()

        val info = stagedBuffer.getExecuteInfo(draw)
        if (info != null) {
            drawFilledBox(info, RenderPipelines.DEBUG_FILLED_BOX)
        }

        stagedBuffer.endFrame()
        //?} else {
        /*prepareFilledBox(poseStack, pos, cameraPos, color, alpha)
        drawFilledBox(RenderPipelines.DEBUG_FILLED_BOX)
        *///?}
    }

    fun renderText(
        text: Component,
        pos: Vec3,
        //~ if <1.21.11 'CameraRenderState' -> 'net.minecraft.client.Camera'
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
        //~ if <1.21.11 'CameraRenderState' -> 'net.minecraft.client.Camera'
        camera: CameraRenderState,
        color: PolyColor,
        backgroundColor: PolyColor,
        disableDepth: Boolean = false,
        dynamic: Boolean = false,
    ) {
        //~ if <1.21.11 'camera.pos' -> 'camera.position' {
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
        //val matrix = Matrix4f().run {
            translate(
                (pos.x - camera.pos.x).toFloat(),
                (pos.y - camera.pos.y).toFloat(),
                (pos.z - camera.pos.z).toFloat()
            )
            //~ if <26.2 'mulPose' -> 'rotate'
            //~ if <1.21.11 '.orientation' -> '.rotation()'
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
        //? if >=1.21.11 {
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
        //? if >=1.21.11 {
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
            BeaconRenderer.SOLID_BEAM_RADIUS,
            BeaconRenderer.BEAM_GLOW_RADIUS
        )
        *///?}
        if (disableDepth) beaconDisableDepth = false

        poseStack.popPose()
    }

    private fun prepareFilledBox(
        poseStack: PoseStack,
        //? if >=26.2
        draw: StagedVertexBuffer.Draw,
        pos: Vec3,
        cameraPos: Vec3,
        color: PolyColor,
        alpha: Float = 0.8f
    ) {
        poseStack.pushPose()
        poseStack.translate(
            pos.x - cameraPos.x,
            pos.y - cameraPos.y,
            pos.z - cameraPos.z
        )

        //? if >=26.2 {
        val builder = stagedBuffer.getVertexBuilder(draw)
        //?} else {
        /*if (buffer == null) {
            buffer = BufferBuilder(
                allocator,
                RenderPipelines.DEBUG_FILLED_BOX.vertexFormatMode,
                RenderPipelines.DEBUG_FILLED_BOX.vertexFormat
            )
        }
        *///?}

        val color = color.withAlpha(alpha)

        //? if >=1.21.11 {
        Shapes.block().forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            addBox(
                poseStack.last().pose(),
                //? if >=26.2 {
                builder,
                //?} else
                //buffer!!,
                minX.toFloat() - Z_FIGHTING_OFFSET,
                minY.toFloat() - Z_FIGHTING_OFFSET,
                minZ.toFloat() - Z_FIGHTING_OFFSET,
                maxX.toFloat() + Z_FIGHTING_OFFSET,
                maxY.toFloat() + Z_FIGHTING_OFFSET,
                maxZ.toFloat() + Z_FIGHTING_OFFSET,
                color.argb
            )
        }
        //?} else {
        /*net.minecraft.client.renderer.ShapeRenderer.addChainedFilledBoxVertices(
            poseStack,
            buffer!!,
            0.0 - Z_FIGHTING_OFFSET,
            0.0 - Z_FIGHTING_OFFSET,
            0.0 - Z_FIGHTING_OFFSET,
            1.0 + Z_FIGHTING_OFFSET,
            1.0 + Z_FIGHTING_OFFSET,
            1.0 + Z_FIGHTING_OFFSET,
            color.redF,
            color.greenF,
            color.blueF,
            color.alphaF
        )
        *///?}

        poseStack.popPose()
    }

    //? if >=1.21.11 {
    private fun addBox(
        positionMatrix: Matrix4fc,
        //~ if <26.2 'VertexConsumer' -> 'BufferBuilder'
        buffer: VertexConsumer,
        minX: Float, minY: Float, minZ: Float,
        maxX: Float, maxY: Float, maxZ: Float,
        color: Int
    ) {
        // front face
        addQuad(buffer, positionMatrix, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, color)
        // back face
        addQuad(buffer, positionMatrix, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, color)
        // left face
        addQuad(buffer, positionMatrix, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, color)
        // right face
        addQuad(buffer, positionMatrix, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, color)
        // top face
        addQuad(buffer, positionMatrix, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, color)
        // bottom face
        addQuad(buffer, positionMatrix, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, color)
    }

    private fun addQuad(
        //~ if <26.2 'VertexConsumer' -> 'BufferBuilder'
        buffer: VertexConsumer,
        positionMatrix: Matrix4fc,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        x4: Float, y4: Float, z4: Float,
        color: Int
    ) {
        buffer.addVertex(positionMatrix, x1, y1, z1).setColor(color)
        buffer.addVertex(positionMatrix, x2, y2, z2).setColor(color)
        buffer.addVertex(positionMatrix, x3, y3, z3).setColor(color)
        buffer.addVertex(positionMatrix, x4, y4, z4).setColor(color)
    }
    //?}

    //? if >=26.2 {
    private fun drawFilledBox(info: StagedVertexBuffer.ExecuteInfo, pipeline: RenderPipeline) {
        val dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(
            RenderSystem.getModelViewMatrixCopy(),
            COLOR_MODULATOR,
            MODEL_OFFSET,
            TEXTURE_MATRIX
        )

        val mainTarget = mc.gameRenderer.mainRenderTarget()
        val colorTexture = mainTarget.colorTextureView ?: return

        RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(
                { "${HytilsRebornConstants.NAME} render pipeline rendering" },
                colorTexture,
                Optional.empty(),
                mainTarget.depthTextureView,
                OptionalDouble.empty()
            ).use { renderPass ->
                renderPass.setPipeline(pipeline)

                RenderSystem.bindDefaultUniforms(renderPass)
                renderPass.setUniform("DynamicTransforms", dynamicTransforms)

                renderPass.setVertexBuffer(0, info.vertexBuffer.slice())
                renderPass.setIndexBuffer(info.indexBuffer, info.indexType)

                renderPass.drawIndexed(info.indexCount, 1, info.firstIndex, info.baseVertex, 0)
            }
    }

    @JvmStatic
    fun close() = stagedBuffer.close()
    //?} else {
    /*private fun drawFilledBox(pipeline: RenderPipeline) {
        val builtBuffer = buffer!!.buildOrThrow()
        val drawParameters = builtBuffer.drawState()
        val format = drawParameters.format()

        val vertices = upload(drawParameters, format, builtBuffer)

        draw(pipeline, builtBuffer, drawParameters, vertices)

        vertexBuffer!!.rotate()
        buffer = null
    }

    private fun upload(drawParameters: DrawState, format: VertexFormat, builtBuffer: MeshData): GpuBuffer {
        val vertexBufferSize = drawParameters.vertexCount() * format.vertexSize

        if (vertexBuffer == null || vertexBuffer!!.size() < vertexBufferSize) {
            vertexBuffer?.close()

            vertexBuffer = MappableRingBuffer(
                { "${HytilsRebornConstants.NAME} render pipeline" },
                GpuBuffer.USAGE_VERTEX or GpuBuffer.USAGE_MAP_WRITE,
                vertexBufferSize
            )
        }

        val commandEncoder = RenderSystem.getDevice().createCommandEncoder()

        commandEncoder.mapBuffer(
            vertexBuffer!!.currentBuffer().slice(0, builtBuffer.vertexBuffer().remaining()/*? if >=1.21.11 {*/ .toLong() /*?}*/),
            false,
            true
        ).use { mappedView ->
            MemoryUtil.memCopy(builtBuffer.vertexBuffer(), mappedView.data())
        }

        return vertexBuffer!!.currentBuffer()
    }

    private fun draw(
        pipeline: RenderPipeline,
        builtBuffer: MeshData,
        drawParameters: DrawState,
        vertices: GpuBuffer
    ) {
        val indices: GpuBuffer?
        val indexType: IndexType?

        if (pipeline.vertexFormatMode == VertexFormat.Mode.QUADS) {
            builtBuffer.sortQuads(allocator, RenderSystem.getProjectionType().vertexSorting())
            indices = pipeline.vertexFormat.uploadImmediateIndexBuffer(builtBuffer.indexBuffer()!!)
            indexType = builtBuffer.drawState().indexType()
        } else {
            val shapeIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.vertexFormatMode)
            indices = shapeIndexBuffer.getBuffer(drawParameters.indexCount())
            indexType = shapeIndexBuffer.type()
        }

        val dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(
            RenderSystem.getModelViewMatrix(),
            COLOR_MODULATOR,
            /*? if >=1.21.11 {*/ MODEL_OFFSET /*?} else {*/ /*RenderSystem.getModelOffset() *//*?}*/,
            /*? if >=1.21.11 {*/ TEXTURE_MATRIX /*?} else {*/ /*RenderSystem.getTextureMatrix() *//*?}*/,
            /*? if <1.21.11 {*/ /*1f *//*?}*/
        )
        RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(
                { "${HytilsRebornConstants.NAME} render pipeline rendering" },
                mc.mainRenderTarget.getColorTextureView()!!,
                OptionalInt.empty(),
                mc.mainRenderTarget.getDepthTextureView(),
                OptionalDouble.empty()
            ).use { renderPass ->
                renderPass.setPipeline(pipeline)
                RenderSystem.bindDefaultUniforms(renderPass)
                renderPass.setUniform("DynamicTransforms", dynamicTransforms)

                renderPass.setVertexBuffer(0, vertices)
                renderPass.setIndexBuffer(indices, indexType)

                renderPass.drawIndexed(0, 0, drawParameters.indexCount(), 1)
            }
        builtBuffer.close()
    }

    @JvmStatic
    fun close() {
        allocator.close()
        vertexBuffer?.close()
        vertexBuffer = null
    }
    *///?}
}
