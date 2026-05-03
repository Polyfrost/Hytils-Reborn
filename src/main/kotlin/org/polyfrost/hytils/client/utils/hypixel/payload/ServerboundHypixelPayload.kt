package org.polyfrost.hytils.client.utils.hypixel.payload

import io.netty.buffer.ByteBuf
import net.hypixel.modapi.packet.HypixelPacket
import net.hypixel.modapi.serializer.PacketSerializer
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

@Suppress("UnstableApiUsage")
class ServerboundHypixelPayload(val packet: HypixelPacket) : CustomPacketPayload {
    private val type: CustomPacketPayload.Type<ServerboundHypixelPayload> =
        CustomPacketPayload.Type(Identifier.parse(packet.identifier))

    private fun write(buf: ByteBuf) {
        val serializer = PacketSerializer(buf)
        packet.write(serializer)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type

    companion object {
        fun buildCodec(): StreamCodec<ByteBuf, ServerboundHypixelPayload> {
            return CustomPacketPayload.codec(
                { payload, buf -> payload.write(buf) },
                { throw UnsupportedOperationException("Cannot read ServerboundHypixelPayload") }
            )
        }
    }
}
