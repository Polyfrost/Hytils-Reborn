package org.polyfrost.hytils.client

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
//? if >=26.1 {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
//?} else if >=1.21.10 {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
*///?} else
//import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.commands.impl.*
import org.polyfrost.hytils.client.data.providers.*
import org.polyfrost.hytils.client.events.PostLevelRenderEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatHandler
import org.polyfrost.hytils.client.features.game.*
import org.polyfrost.hytils.client.features.game.titles.CountdownTitles
import org.polyfrost.hytils.client.features.game.titles.GameEndingTitles
import org.polyfrost.hytils.client.features.game.titles.GameStartingTitles
import org.polyfrost.hytils.client.features.general.ArmorStandHider
import org.polyfrost.hytils.client.features.limbo.LimboLimiter
import org.polyfrost.hytils.client.features.limbo.LimboPrivateMessageSounds
import org.polyfrost.hytils.client.features.lobby.SilentLobby
import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HytilsRebornClient {
    val LOGGER: Logger = LoggerFactory.getLogger(HytilsRebornConstants.NAME)

    fun initialize() {
        HytilsRebornConfig.preload()

        Multithreading.submit {
            listOf(
                LanguageData, ArmorStandData, ChatEmotesData, CosmeticsData,
                GameAliasesData, GameIdentifiersData, HeightLimitData
            ).forEach { it.load() }
        }

        listOf(
            // misc features
            ChatHandler, SilentLobby,

            // game features
            BlockCountNotifier, CountdownTitles,
            GameEndingTitles, GameStartingTitles,
            ChestHighlighter, HardcoreStatus, HeightOverlay,
            MiniWallsMiddleBeacon, PitLagReducer,
            SumoRenderDistance, UHCMiddleWaypoint,

            // general features
            ArmorStandHider,

            // limbo features
            LimboLimiter, LimboPrivateMessageSounds,
        ).forEach { EventManager.INSTANCE.register(it) }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            listOf(
                HytilsCommand, HousingVisitCommand,
                PlayCommand, RequeueCommand,
                SilentRemoveCommand, SkyblockVisitCommand,
            ).forEach {
                val command = dispatcher.register(it.getCommand())
                it.getAliases().forEach { alias ->
                    // https://github.com/Mojang/brigadier/issues/46
                    val builder = LiteralArgumentBuilder.literal<FabricClientCommandSource>(alias)
                        .requires(command.requirement)
                        .forward(command.redirect, command.redirectModifier, command.isFork)
                        .executes(command.command)
                        .apply { command.children?.forEach { child -> then(child) } }
                    dispatcher.register(builder)
                }
            }
        }

        //~ if <26.1 'LevelRenderEvents' -> 'WorldRenderEvents'
        //~ if <1.21.10 'END_MAIN' -> 'LAST'
        LevelRenderEvents.END_MAIN.register { context ->
            EventManager.INSTANCE.post(
                PostLevelRenderEvent(
                    //? if >=26.1 {
                    context.poseStack(),
                    context.submitNodeCollector(),
                    //? if <26.2
                    //context.bufferSource(),
                    context.levelState().cameraRenderState
                    //?} else if >=1.21.10 {
                    /*context.matrices(),
                    context.commandQueue(),
                    context.consumers(),
                    context.worldState().cameraRenderState
                    *///?} else {
                    /*context.matrixStack()!!,
                    context.consumers()!!,
                    context.camera()!!
                    *///?}
                )
            )
            //? if >=26.1 <26.2
            //context.bufferSource().endBatch()
        }

        HypixelModAPIImpl.init()
    }
}
