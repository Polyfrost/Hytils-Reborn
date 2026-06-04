package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.commands.ClientCommand
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import java.util.concurrent.TimeUnit

object SkyblockVisitCommand : ClientCommand {
    private var playerUsername = ""

    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("skyblockvisit")
        .requires { HypixelUtils.isHypixel() }
        .then(ClientCommandManager.argument("username", StringArgumentType.word())
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
                mc.player?.connection?.sendChat("/visit $playerUsername")
            } else {
                mc.player?.connection?.sendChat("/play skyblock")
                EventManager.INSTANCE.register(SkyblockVisitHook, true)
            }
        } else {
            mc.player?.displayClientMessage(
                Component.literal("Invalid username!").withStyle(ChatFormatting.RED),
                false
            )
        }
    }

    private object SkyblockVisitHook {
        @Subscribe
        fun onWorldLoad(event: WorldEvent.Load) {
            Multithreading.schedule(
                { mc.player?.connection?.sendChat("/visit $playerUsername") },
                300L,
                TimeUnit.MILLISECONDS
            )
            EventManager.INSTANCE.unregister(this)
        }
    }
}
