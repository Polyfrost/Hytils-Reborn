package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.features.chat.handlers.modules.triggers.SilentRemoval
import org.polyfrost.hytils.client.data.providers.LanguageData
import java.util.Locale

object SilentRemoveCommand : ClientCommand {
    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("silentremove")
        .then(ClientCommandManager.literal("add")
            .then(ClientCommandManager.argument("username", StringArgumentType.string())
                .executes { context ->
                    val username = context.getArgument("username", String::class.java)
                    executeAdd(username.lowercase(Locale.ROOT))
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(ClientCommandManager.literal("remove")
            .then(ClientCommandManager.argument("username", StringArgumentType.string())
                .executes { context ->
                    val username = context.getArgument("username", String::class.java)
                    executeRemove(username.lowercase(Locale.ROOT))
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(ClientCommandManager.literal("list")
            .executes {
                executeList()
                Command.SINGLE_SUCCESS
            }
        )
        .then(ClientCommandManager.literal("clear")
            .executes {
                executeClear()
                Command.SINGLE_SUCCESS
            }
        )

    override fun getAliases() = listOf("sremove")

    private fun executeAdd(username: String) {
        if (!username.matches(LanguageData.VALID_USERNAME)) {
            OmniClientChat.displayChatMessage(
                Text.literal("Invalid username!").setStyle(MCTextStyle.color(TextColors.RED))
            )
            return
        }

        if (!SilentRemoval.removalQueue.contains(username)) {
            SilentRemoval.removalQueue.add(username)
            OmniClientChat.displayChatMessage(
                Text.literal("Added ").setStyle(MCTextStyle.color(TextColors.GREEN))
                    .append(Text.literal(username).setStyle(MCTextStyle.color(TextColors.YELLOW)))
                    .append(Text.literal(" to the silent removal queue.").setStyle(MCTextStyle.color(TextColors.GREEN)))
            )
        } else {
            OmniClientChat.displayChatMessage(
                Text.literal("$username is already in the silent removal queue!").setStyle(MCTextStyle.color(TextColors.RED))
            )
        }
    }

    private fun executeRemove(username: String) {
        if (!username.matches(LanguageData.VALID_USERNAME)) {
            OmniClientChat.displayChatMessage(
                Text.literal("Invalid username!").setStyle(MCTextStyle.color(TextColors.RED))
            )
            return
        }

        if (SilentRemoval.removalQueue.contains(username)) {
            SilentRemoval.removalQueue.remove(username)
            OmniClientChat.displayChatMessage(
                Text.literal("Removed ").setStyle(MCTextStyle.color(TextColors.GREEN))
                    .append(Text.literal(username).setStyle(MCTextStyle.color(TextColors.YELLOW)))
                    .append(Text.literal(" from the silent removal queue.").setStyle(MCTextStyle.color(TextColors.GREEN)))
            )
        } else {
            OmniClientChat.displayChatMessage(
                Text.literal("$username is not in the silent removal queue!").setStyle(MCTextStyle.color(TextColors.RED))
            )
        }
    }

    private fun executeList() {
        if (SilentRemoval.removalQueue.isEmpty()) {
            OmniClientChat.displayChatMessage(
                Text.literal("The silent removal queue is empty.").setStyle(MCTextStyle.color(TextColors.YELLOW))
            )
        } else {
            OmniClientChat.displayChatMessage(
                Text.literal("Silent removal queue: ").setStyle(MCTextStyle.color(TextColors.GREEN))
                    .apply {
                        SilentRemoval.removalQueue.forEachIndexed { index, username ->
                            append(Text.literal(username).setStyle(MCTextStyle.color(TextColors.YELLOW)))
                            if (index < SilentRemoval.removalQueue.size - 1) {
                                append(Text.literal(", ").setStyle(MCTextStyle.color(TextColors.GREEN)))
                            }
                        }
                    }
            )
        }
    }

    private fun executeClear() {
        if (SilentRemoval.removalQueue.isEmpty()) {
            OmniClientChat.displayChatMessage(
                Text.literal("The silent removal queue is already empty.").setStyle(MCTextStyle.color(TextColors.YELLOW))
            )
        } else {
            SilentRemoval.removalQueue.clear()
            OmniClientChat.displayChatMessage(
                Text.literal("Cleared the silent removal queue.").setStyle(MCTextStyle.color(TextColors.GREEN))
            )
        }
    }
}
