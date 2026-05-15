package org.polyfrost.hytils.client.handlers.chat

import org.polyfrost.hytils.client.HytilsRebornClient
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.events.ChatSendEvent
import org.polyfrost.hytils.client.handlers.chat.modules.blockers.*
import org.polyfrost.hytils.client.handlers.chat.modules.modifiers.*
import org.polyfrost.hytils.client.handlers.chat.modules.triggers.*
import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe

object ChatHandler {
    private val receiveModules = mutableListOf<ChatReceiveModule>()
    private val sendModules = mutableListOf<ChatSendModule>()
    private val resetModules = mutableListOf<ChatReceiveResetModule>()

    init {
        listOf(
            // blockers
            AdBlocker, AntiGG, AntiGL, BedwarsAdvertisementsRemover,
            BridgeOwnGoalDeathRemover, ConnectionStatusRemover,
            CurseOfSpamRemover, DiscordSafetyWarningRemover, DuelsBlockTrail,
            DuelsNoStatsChange, EarnedCoinsAndExpRemover, GameAnnouncementsRemover,
            GameTipsRemover, GiftBlocker, GuildMOTD, HotPotatoRemover,
            HypeLimitReminderRemover, KarmaRemover, LobbyFishingAnnouncementRemover,
            LobbyJoinRemover, NonCooldownBlocker, OnlineStatusRemover, QuestBlocker,
            ReplayRecordedRemover, SeasonalCollectedRemover, ServerConnectedMessage,
            ShoutBlocker, SkyblockWelcomeRemover, SoulWellAnnouncerRemover,
            StatsMessageRemover, TicketMachineRemover, TipMessageRemover,

            // modifiers
            ChatEmoteReplacer, ColoredPlayerConnectionStatus,
            GameStartCompactor, GameStatusRestyler, ShortChannelNames,
            ShortPMChannelNames, WhiteNonChat, WhitePrivateMessages,

            // triggers
            AutoAFKReply, AutoChatSwapper, AutoCheckStats, AutoFriend,
            AutoGG, AutoGL, AutoPartyWarn, AutoPartyWarpConfirm,
            AutoQueue, AutoWB, GuildWelcomer, BroadcastAchievement,
            BroadcastLevelUp, SilentRemoval, ThankWatchdog
        ).forEach { this.registerModule(it) }

        // needs additional event listeners
        listOf(
            GuildMOTD, AutoCheckStats, AutoGG, AutoQueue
        ).forEach { EventManager.INSTANCE.register(it) }

        receiveModules.sortBy { it.priority }
        sendModules.sortBy { it.priority }
        resetModules.sortBy { it.priority }
    }

    @Subscribe
    fun onChatReceive(event: ChatReceiveEvent) {
        if (!HytilsRebornConfig.isEnabled || !HypixelModAPIImpl.onHypixel || event.isOverlay) return

        for (module in this.receiveModules) {
            try {
                if (module.isEnabled) {
                    module.onChatReceived(event)
                    if (event.cancelled) return
                }
            } catch (e: Exception) {
                HytilsRebornClient.LOGGER.error(
                    "An error occurred while handling a received chat message with module ${module.javaClass.getSimpleName()}",
                    e
                )
            }
        }
    }

    @Subscribe
    fun onChatSend(event: ChatSendEvent) {
        if (!HytilsRebornConfig.isEnabled || !HypixelModAPIImpl.onHypixel) return

        for (module in this.sendModules) {
            try {
                if (module.isEnabled) {
                    module.onChatSend(event)
                    if (event.cancelled) return
                }
            } catch (e: Exception) {
                HytilsRebornClient.LOGGER.error(
                    "An error occurred while handling a sent chat message with module ${module.javaClass.getSimpleName()}",
                    e
                )
            }
        }
    }

    @Subscribe
    fun onWorldLeave(event: WorldEvent.Unload) {
        for (module in this.resetModules) {
            module.reset()
        }
    }

    private fun registerModule(module: ChatModule) {
        if (module is ChatReceiveModule) receiveModules.add(module)
        if (module is ChatSendModule) sendModules.add(module)
        if (module is ChatReceiveResetModule) resetModules.add(module)
    }
}
