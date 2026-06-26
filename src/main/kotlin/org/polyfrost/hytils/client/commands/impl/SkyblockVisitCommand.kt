package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object SkyblockVisitCommand : ClientCommand {
    private var playerUsername = ""

    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommands.literal("skyblockvisit")
        .requires { HypixelUtils.isHypixel() }
        .then(ClientCommands.argument("username", StringArgumentType.word())
            .executes { context ->
                val username = context.getArgument("username", String::class.java)
                execute(username)
                Command.SINGLE_SUCCESS
            }
        )

    override fun getAliases() = listOf("sbvisit")

    private fun execute(username: String) {
        if (username.matches(LanguageData.VALID_USERNAME)) {
            playerUsername = username

            val location = HypixelUtils.getLocation()

            if (!location.inGame() && location.gameType.orElse(null) == GameType.HOUSING) {
                ChatUtils.sendMessage("/visit $playerUsername")
            } else {
                ChatUtils.sendMessage("/play skyblock")
                EventManager.INSTANCE.register(SkyblockVisitHook, true)
            }
        } else {
            ChatUtils.displayMessage(Component.literal("Invalid username!").withStyle(ChatFormatting.RED))
        }
    }

    private object SkyblockVisitHook {
        @Subscribe
        fun onWorldLoad(event: WorldEvent.Load) {
            Multithreading.schedule(
                { ChatUtils.queueMessage("/visit $playerUsername") },
                300L,
                TimeUnit.MILLISECONDS
            )
            EventManager.INSTANCE.unregister(this)
        }
    }
}
