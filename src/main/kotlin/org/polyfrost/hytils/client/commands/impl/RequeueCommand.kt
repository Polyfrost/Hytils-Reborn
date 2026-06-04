package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.data.providers.GameIdentifiersData
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object RequeueCommand : ClientCommand {
    private var game = ""

    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommands.literal("requeue")
        .requires { HypixelUtils.isHypixel() }
        .executes {
            execute()
            Command.SINGLE_SUCCESS
        }

    override fun getAliases() = listOf("rq")

    private fun execute() {
        val location = HypixelUtils.getLocation()
        if (location.inGame()) {
            game = location.mode.orElse(null) ?: run {
                ChatUtils.displayMessage(
                    Component.literal("You must be in a valid game to use this command.").withStyle(ChatFormatting.RED)
                )
                return
            }

            when (location.gameType.orElse(null)) {
                GameType.SKYBLOCK, GameType.HOUSING, GameType.REPLAY -> {
                    ChatUtils.displayMessage(
                        Component.literal("You must be in a valid game to use this command.").withStyle(ChatFormatting.RED)
                    )
                    return
                }
                else -> {}
            }
        } else if (location.wasInGame()) {
            game = location.lastMode.orElse(null) ?: run {
                ChatUtils.displayMessage(
                    Component.literal("The last round has to be a valid game to use this command.").withStyle(ChatFormatting.RED)
                )
                return
            }

            when (location.lastGameType.orElse(null)) {
                GameType.SKYBLOCK -> game = GameType.SKYBLOCK.databaseName
                GameType.HOUSING, GameType.REPLAY -> {
                    ChatUtils.displayMessage(
                        Component.literal("The last round has to be a valid game to use this command.").withStyle(ChatFormatting.RED)
                    )
                    return
                }
                else -> {}
            }
        } else {
            ChatUtils.displayMessage(
                Component.literal("The last round has to be a game to use this command.").withStyle(ChatFormatting.RED)
            )
            return
        }

        if (location.gameType.isPresent) {
            val databaseName = location.gameType.get().databaseName
            val identifier = GameIdentifiersData.games[databaseName]?.get(game) ?: game
            game = identifier
        }

        ChatUtils.sendMessage("/play $game")
    }
}
