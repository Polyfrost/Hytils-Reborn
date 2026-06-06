package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.commands.parser.GameNameArgumentType
import org.polyfrost.hytils.client.data.providers.GameAliasesData
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import java.util.Locale

object PlayCommand : ClientCommand {
    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommands.literal("play")
        .requires { HypixelUtils.isHypixel() }
        .then(ClientCommands.argument("game", GameNameArgumentType.gameName(GameAliasesData.aliases))
            .executes { context ->
                val game = context.getArgument("game", GameNameArgumentType.GameName::class.java)
                execute(game)
                Command.SINGLE_SUCCESS
            }
        )

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = getCommand().build()

    private fun execute(game: GameNameArgumentType.GameName) {
        val command = if (HytilsRebornConfig.autocompletePlayCommands) {
            when {
                GameAliasesData.aliases.containsKey(game.name.lowercase(Locale.ROOT)) -> GameAliasesData.aliases[game.name.lowercase(Locale.ROOT)]
                GameAliasesData.aliases.containsValue(game.name.lowercase(Locale.ROOT)) -> game.name
                else -> {
                    ChatUtils.displayMessage(Component.literal("Invalid game: \"${game.name}\"").withStyle(ChatFormatting.RED))
                    return
                }
            }
        } else {
            game.name
        }

        ChatUtils.sendMessage("/play $command")
    }
}
