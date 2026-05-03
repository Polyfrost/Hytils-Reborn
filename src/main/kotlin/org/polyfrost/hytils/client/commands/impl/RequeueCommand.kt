package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.data.providers.GameIdentifiersData
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object RequeueCommand : ClientCommand {
    private var game = ""

    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("requeue")
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
                OmniClientChat.displayChatMessage(
                    Text.literal("You must be in a valid game to use this command.")
                        .setStyle(MCTextStyle.color(TextColors.RED))
                )
                return
            }

            when (location.gameType.orElse(null)) {
                GameType.SKYBLOCK, GameType.HOUSING, GameType.REPLAY -> {
                    OmniClientChat.displayChatMessage(
                        Text.literal("You must be in a valid game to use this command.")
                            .setStyle(MCTextStyle.color(TextColors.RED))
                    )
                    return
                }
                else -> {}
            }
        } else if (location.wasInGame()) {
            game = location.lastMode.orElse(null) ?: run {
                OmniClientChat.displayChatMessage(
                    Text.literal("You must be in a valid game to use this command.")
                        .setStyle(MCTextStyle.color(TextColors.RED))
                )
                return
            }

            when (location.lastGameType.orElse(null)) {
                GameType.SKYBLOCK -> game = GameType.SKYBLOCK.databaseName
                GameType.HOUSING, GameType.REPLAY -> {
                    OmniClientChat.displayChatMessage(
                        Text.literal("The last round has to be a valid game to use this command.")
                            .setStyle(MCTextStyle.color(TextColors.RED))
                    )
                    return
                }
                else -> {}
            }
        } else {
            OmniClientChat.displayChatMessage(
                Text.literal("The last round has to be a game to use this command.")
                    .setStyle(MCTextStyle.color(TextColors.RED))
            )
            return
        }

        if (location.gameType.isPresent) {
            val databaseName = location.gameType.get().databaseName
            val identifier = GameIdentifiersData.games[databaseName]?.get(game) ?: game
            game = identifier
        }

        OmniClientChatSender.send("/play $game") // FIXME: .queue
    }
}
