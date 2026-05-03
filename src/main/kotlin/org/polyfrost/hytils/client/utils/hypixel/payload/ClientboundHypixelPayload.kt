package org.polyfrost.hytils.client.utils.hypixel.payload

import io.netty.buffer.ByteBuf
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.error.ErrorReason
import net.hypixel.modapi.packet.ClientboundHypixelPacket
import net.hypixel.modapi.serializer.PacketSerializer
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Suppress("UnstableApiUsage")
class ClientboundHypixelPayload private constructor(
    private val type: CustomPacketPayload.Type<ClientboundHypixelPayload>,
    buf: ByteBuf
) : CustomPacketPayload {
    val packet: ClientboundHypixelPacket?
    val errorReason: ErrorReason?

    init {
        val serializer = PacketSerializer(buf)
        val success = serializer.readBoolean()

        if (!success) {
            this.errorReason = ErrorReason.getById(serializer.readVarInt())
            this.packet = null
        } else {
            this.errorReason = null
            this.packet = HypixelModAPI.getInstance().registry.createClientboundPacket(type.id().toString(), serializer)
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type

    fun isSuccess(): Boolean = packet != null

    companion object {
        fun buildCodec(id: CustomPacketPayload.Type<ClientboundHypixelPayload>): StreamCodec<ByteBuf, ClientboundHypixelPayload> {
            return CustomPacketPayload.codec(
                { _, _ -> throw UnsupportedOperationException("Cannot write ClientboundHypixelPayload") },
                { buf -> ClientboundHypixelPayload(id, buf) }
            )
        }
    }
}
