package org.polyfrost.hytils.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

interface ClientCommand {
    fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource>
    fun getAliases(): List<String> = emptyList()
}
