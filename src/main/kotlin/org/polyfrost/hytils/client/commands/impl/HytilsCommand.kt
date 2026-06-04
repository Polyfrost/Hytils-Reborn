package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.commands.parser.GEXPTypeArgumentType
import org.polyfrost.hytils.client.commands.parser.GameArgumentType
import org.polyfrost.hytils.client.utils.HypixelAPIUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
import java.util.Locale

// TODO: notifications (or just use chat messages)
object HytilsCommand : ClientCommand {
    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("hytils")
        .executes { _ ->
            HytilsRebornConfig.openUI()
            Command.SINGLE_SUCCESS
        }
        .then(ClientCommandManager.literal("gexp")
            .executes { _ ->
                Multithreading.submit { executeGEXP(null, null) }
                Command.SINGLE_SUCCESS
            }
            .then(ClientCommandManager.argument("username", StringArgumentType.string())
                .executes { context ->
                    val username = context.getArgument("username", String::class.java)
                    Multithreading.submit { executeGEXP(username, null) }
                    Command.SINGLE_SUCCESS
                }
                .then(ClientCommandManager.argument("type", GEXPTypeArgumentType.gexpType())
                    .executes { context ->
                        val username = context.getArgument("username", String::class.java)
                        val type = context.getArgument("type", GEXPTypeArgumentType.GEXPType::class.java)
                        Multithreading.submit { executeGEXP(username, type) }
                        Command.SINGLE_SUCCESS
                    }
                )
            )
        )
        .then(ClientCommandManager.literal("winstreak")
            .then(ClientCommandManager.argument("username", StringArgumentType.string())
                .then(ClientCommandManager.argument("gamemode", GameArgumentType.gameType())
                    .executes { context ->
                        val username = context.getArgument("username", String::class.java)
                        val game = context.getArgument("gamemode", GameArgumentType.Game::class.java)
                        Multithreading.submit { executeWinstreak(username, game) }
                        Command.SINGLE_SUCCESS
                    }
                )
            )
        )

    override fun getAliases() = listOf("hytilsreborn", "hytil", "hytilities", "hytilitiesreborn")

    private fun executeGEXP(username: String?, type: GEXPTypeArgumentType.GEXPType?) {
        val gexp = when {
            username == null -> HypixelAPIUtils.getGEXP()
            type == GEXPTypeArgumentType.GEXPType.WEEKLY -> HypixelAPIUtils.getWeeklyGEXP(username)
            else -> HypixelAPIUtils.getGEXP(username)
        }

        val label = when (type) {
            GEXPTypeArgumentType.GEXPType.DAILY -> " daily"
            GEXPTypeArgumentType.GEXPType.WEEKLY -> " weekly"
            null -> ""
        }

        if (gexp != null) {
            val subject = if (username != null) "$username currently has" else "You currently have"
//            mc.execute {
//                Notifications.enqueue(
//                    Notifications.Type.Info,
//                    HytilsRebornConstants.NAME,
//                    "$subject $gexp$label guild EXP."
//                )
//            }
        } else {
            val target = if (username != null) "$username's" else "your"
//            mc.execute {
//                Notifications.enqueue(
//                    Notifications.Type.Error,
//                    HytilsRebornConstants.NAME,
//                    "There was a problem trying to get $target$label guild EXP."
//                )
//            }
        }
    }

    private fun executeWinstreak(username: String, game: GameArgumentType.Game) {
        val isSelf = username == mc.user.name

        val winstreak = HypixelAPIUtils.getWinstreak(username, game.name)

        val label = if (!isSelf) {
            " in ${game.name.lowercase(Locale.ROOT)}"
        } else ""

        if (winstreak != null) {
            val subject = if (!isSelf) "$username currently has" else "You currently have"
//            mc.execute {
//                Notifications.enqueue(
//                    Notifications.Type.Info,
//                    HytilsRebornConstants.NAME,
//                    "$subject a $winstreak winstreak$label."
//                )
//            }
        } else {
            val target = if (!isSelf) "$username's" else "your"
//            mc.execute {
//                Notifications.enqueue(
//                    Notifications.Type.Error,
//                    HytilsRebornConstants.NAME,
//                    "There was a problem trying to get $target winstreak."
//                )
//            }
        }
    }
}
