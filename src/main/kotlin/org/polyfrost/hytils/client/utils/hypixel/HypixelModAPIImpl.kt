/**
 * Copyright (c) 2024 Hypixel
 * SPDX-License-Identifier: MIT
 * 
 * Heavily adapted from Hypixel/FabricModAPI under the MIT license.
 * https://github.com/HypixelDev/FabricModAPI
 */

package org.polyfrost.hytils.client.utils.hypixel

import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.HypixelModAPIImplementation
import net.hypixel.modapi.packet.HypixelPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.utils.hypixel.payload.ClientboundHypixelPayload
import org.polyfrost.hytils.client.utils.hypixel.payload.ServerboundHypixelPayload
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// FIXME: OneConfig's Mod API implementation does not properly register packets, so we have to use our own implementation.
@Suppress("UnstableApiUsage")
object HypixelModAPIImpl : HypixelModAPIImplementation {
    private val LOGGER: Logger = LoggerFactory.getLogger("${HytilsRebornConstants.NAME}/${this::class.simpleName}")

    @JvmStatic var onHypixel = false

    @JvmStatic
    fun init() {
        HypixelModAPI.getInstance().setModImplementation(this)
    }

    override fun onInit() {
        //~ if <1.21.11 'Identifier' -> 'Identifier' {
        for (identifier in HypixelModAPI.getInstance().registry.clientboundIdentifiers) {
            try {
                registerClientbound(identifier)
                LOGGER.info("Registered clientbound packet with identifier '{}'", identifier)
            } catch (e: Exception) {
                LOGGER.error("Failed to register clientbound packet with identifier '{}'", identifier, e)
            }
        }

        for (identifier in HypixelModAPI.getInstance().registry.serverboundIdentifiers) {
            try {
                registerServerbound(identifier)
                LOGGER.info("Registered serverbound packet with identifier '{}'", identifier)
            } catch (e: java.lang.Exception) {
                LOGGER.error("Failed to register serverbound packet with identifier '{}'", identifier, e)
            }
        }
        //~}

        HypixelModAPI.getInstance().createHandler(ClientboundHelloPacket::class.java) { onHypixel = true }
        ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> onHypixel = false }
    }

    override fun sendPacket(packet: HypixelPacket): Boolean {
        if (!isConnectedToHypixel()) return false

        val hypixelPayload = ServerboundHypixelPayload(packet)

        LOGGER.info("Sending packet ${packet::class.simpleName} with identifier '${hypixelPayload.type().id.path}'")

        if (mc.connection != null) {
            ClientPlayNetworking.send(hypixelPayload)
            return true
        }

        try {
            ClientConfigurationNetworking.send(hypixelPayload)
            return true
        } catch (_: IllegalStateException) {
            LOGGER.warn("Failed to send a packet as the client is not connected to a server '{}'", packet)
            return false
        }
    }

    override fun isConnectedToHypixel() = onHypixel

    private fun registerClientbound(identifier: String) {
        val clientboundId = CustomPacketPayload.Type<ClientboundHypixelPayload>(Identifier.parse(identifier))
        val codec: StreamCodec<ByteBuf, ClientboundHypixelPayload> = ClientboundHypixelPayload.buildCodec(clientboundId)
        //~ if <26.1 'clientboundPlay()' -> 'playS2C()'
        PayloadTypeRegistry.clientboundPlay().register(clientboundId, codec)
        //~ if <26.1 'clientboundConfiguration()' -> 'configurationS2C()'
        PayloadTypeRegistry.clientboundConfiguration().register(clientboundId, codec)

        ClientPlayNetworking.registerGlobalReceiver(clientboundId) { payload, _ ->
            LOGGER.debug("Received packet with identifier '{}', during PLAY", identifier)
            handleIncomingPayload(identifier, payload)
        }
        ClientConfigurationNetworking.registerGlobalReceiver(clientboundId) { payload, _ ->
            LOGGER.debug("Received packet with identifier '{}', during CONFIGURATION", identifier)
            handleIncomingPayload(identifier, payload)
        }
    }

    private fun handleIncomingPayload(identifier: String?, payload: ClientboundHypixelPayload) {
        if (!payload.isSuccess()) {
            LOGGER.warn("Received an error response for packet {}: {}", identifier, payload.errorReason)
            try {
                HypixelModAPI.getInstance().handleError(identifier, payload.errorReason)
            } catch (e: Exception) {
                LOGGER.error("An error occurred while handling error response for packet {}", identifier, e)
            }
            return
        }

        LOGGER.info("Handling packet ${payload.packet!!::class.simpleName} with identifier '$identifier'")

        try {
            HypixelModAPI.getInstance().handle(payload.packet)
        } catch (e: Exception) {
            LOGGER.error("An error occurred while handling packet {}", identifier, e)
        }
    }

    private fun registerServerbound(identifier: String) {
        val serverboundId = CustomPacketPayload.Type<ServerboundHypixelPayload>(Identifier.parse(identifier))
        val codec: StreamCodec<ByteBuf, ServerboundHypixelPayload> = ServerboundHypixelPayload.buildCodec()
        //~ if <26.1 'serverboundPlay()' -> 'playC2S()'
        PayloadTypeRegistry.serverboundPlay().register(serverboundId, codec)
        //~ if <26.1 'serverboundConfiguration()' -> 'configurationC2S()'
        PayloadTypeRegistry.serverboundConfiguration().register(serverboundId, codec)
    }
}
