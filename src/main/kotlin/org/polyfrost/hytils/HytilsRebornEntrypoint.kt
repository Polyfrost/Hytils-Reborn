package org.polyfrost.hytils

import net.fabricmc.api.ClientModInitializer
import org.polyfrost.hytils.client.HytilsRebornClient

class HytilsRebornEntrypoint : ClientModInitializer {
    override fun onInitializeClient() = HytilsRebornClient.initialize()
}
