package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.features.chat.handlers.modules.triggers.SilentRemoval
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.utils.ChatUtils
import java.util.Locale

object SilentRemoveCommand : ClientCommand {
    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommands.literal("silentremove")
        .then(ClientCommands.literal("add")
            .then(ClientCommands.argument("username", StringArgumentType.string())
                .executes { context ->
                    val username = context.getArgument("username", String::class.java)
                    executeAdd(username.lowercase(Locale.ROOT))
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(ClientCommands.literal("remove")
            .then(ClientCommands.argument("username", StringArgumentType.string())
                .executes { context ->
                    val username = context.getArgument("username", String::class.java)
                    executeRemove(username.lowercase(Locale.ROOT))
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(ClientCommands.literal("list")
            .executes {
                executeList()
                Command.SINGLE_SUCCESS
            }
        )
        .then(ClientCommands.literal("clear")
            .executes {
                executeClear()
                Command.SINGLE_SUCCESS
            }
        )

    override fun getAliases() = listOf("sremove")

    private fun executeAdd(username: String) {
        if (!username.matches(LanguageData.VALID_USERNAME)) {
            ChatUtils.displayMessage(Component.literal("Invalid username!").withStyle(ChatFormatting.RED))
            return
        }

        if (!SilentRemoval.removalQueue.contains(username)) {
            SilentRemoval.removalQueue.add(username)
            ChatUtils.displayMessage(
                Component.literal("Added ").withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(username).withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(" to the silent removal queue.").withStyle(ChatFormatting.GREEN))
            )
        } else {
            ChatUtils.displayMessage(
                Component.literal("$username is already in the silent removal queue!").withStyle(ChatFormatting.RED)
            )
        }
    }

    private fun executeRemove(username: String) {
        if (!username.matches(LanguageData.VALID_USERNAME)) {
            ChatUtils.displayMessage(Component.literal("Invalid username!").withStyle(ChatFormatting.RED))
            return
        }

        if (SilentRemoval.removalQueue.contains(username)) {
            SilentRemoval.removalQueue.remove(username)
            ChatUtils.displayMessage(
                Component.literal("Removed ").withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(username).withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(" from the silent removal queue.").withStyle(ChatFormatting.GREEN))
            )
        } else {
            ChatUtils.displayMessage(
                Component.literal("$username is not in the silent removal queue!").withStyle(ChatFormatting.RED)
            )
        }
    }

    private fun executeList() {
        if (SilentRemoval.removalQueue.isEmpty()) {
            ChatUtils.displayMessage(
                Component.literal("The silent removal queue is empty.").withStyle(ChatFormatting.YELLOW)
            )
        } else {
            ChatUtils.displayMessage(
                Component.literal("Silent removal queue: ").withStyle(ChatFormatting.GREEN)
                    .apply {
                        SilentRemoval.removalQueue.forEachIndexed { index, username ->
                            append(Component.literal(username).withStyle(ChatFormatting.YELLOW))
                            if (index < SilentRemoval.removalQueue.size - 1) {
                                append(Component.literal(", ").withStyle(ChatFormatting.GREEN))
                            }
                        }
                    }
            )
        }
    }

    private fun executeClear() {
        if (SilentRemoval.removalQueue.isEmpty()) {
            ChatUtils.displayMessage(
                Component.literal("The silent removal queue is already empty.").withStyle(ChatFormatting.YELLOW)
            )
        } else {
            SilentRemoval.removalQueue.clear()
            ChatUtils.displayMessage(
                Component.literal("Cleared the silent removal queue.").withStyle(ChatFormatting.GREEN)
            )
        }
    }
}
