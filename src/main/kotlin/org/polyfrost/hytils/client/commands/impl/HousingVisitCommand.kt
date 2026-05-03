package org.polyfrost.hytils.client.commands.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
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
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object HousingVisitCommand : ClientCommand {
    private var playerUsername = ""

    override fun getCommand(): LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("housingvisit")
        .requires { HypixelUtils.isHypixel() }
        .then(ClientCommandManager.argument("username", StringArgumentType.word())
            .executes { context ->
                val username = context.getArgument("username", String::class.java)
                execute(username)
                Command.SINGLE_SUCCESS
            }
        )

    override fun getAliases() = listOf("hvisit")

    private fun execute(username: String) {
        if (username.matches(LanguageData.VALID_USERNAME)) {
            playerUsername = username

            val location = HypixelUtils.getLocation()

            if (!location.inGame() && location.gameType.orElse(null) == GameType.HOUSING) {
                OmniClientChatSender.send("/visit $playerUsername") // FIXME: .queue
            } else {
                OmniClientChatSender.send("/l housing")
                EventManager.INSTANCE.register(HousingVisitHook, true)
            }
        } else {
            OmniClientChat.displayChatMessage(
                Text.literal("Invalid username!").setStyle(MCTextStyle.color(TextColors.RED))
            )
        }
    }

    private object HousingVisitHook {
        @Subscribe
        fun onWorldLoad(event: WorldEvent.Load) {
            Multithreading.schedule(
                { OmniClientChatSender.send("/visit $playerUsername") },
                300L,
                TimeUnit.MILLISECONDS
            )
            EventManager.INSTANCE.unregister(this)
        }
    }
}
