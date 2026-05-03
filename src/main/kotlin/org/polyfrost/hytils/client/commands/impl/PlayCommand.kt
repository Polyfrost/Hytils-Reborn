package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.commands.parser.GameNameArgumentType
import org.polyfrost.hytils.client.data.providers.GameAliasesData
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.Locale

object PlayCommand : ClientCommand {
    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("play")
        .requires { HypixelUtils.isHypixel() }
        .then(ClientCommandManager.argument("game", GameNameArgumentType.gameName(GameAliasesData.aliases))
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
                    OmniClientChat.displayChatMessage(
                        Text.literal("Invalid game: \"${game.name}\"").setStyle(MCTextStyle.color(TextColors.RED))
                    )
                    return
                }
            }
        } else {
            game.name
        }

        OmniClientChatSender.send("/play $command") // FIXME: .queue
    }
}
