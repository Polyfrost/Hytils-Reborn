package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import net.fabricmc.loader.api.FabricLoader
import org.polyfrost.chatting.chat.ChatTabs
import org.polyfrost.chatting.config.ChattingConfig
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils
import org.polyfrost.oneconfig.api.notifications.v1.NotificationAction
import org.polyfrost.oneconfig.api.notifications.v1.NotificationType
import org.polyfrost.oneconfig.api.notifications.v1.Notifications
import org.polyfrost.oneconfig.utils.v1.Multithreading
import java.util.concurrent.TimeUnit

object AutoChatSwapper : ChatReceiveModule {
    private var shouldCancelChannelMessage = false

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.plainMessage.matches(LanguageData.PARTY_JOIN)) {
            ChatUtils.queueMessage("/chat party")
            switchChattingTab("party")

            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (event.plainMessage.matches(LanguageData.PARTY_LEAVE)) {
            val channel = when (HytilsRebornConfig.chatSwapperReturnChannel) {
                1 -> "guild"
                2 -> "officer"
                else -> "all"
            }

            ChatUtils.queueMessage("/chat $channel")
            switchChattingTab(channel)

            shouldCancelChannelMessage = true
            Multithreading.schedule({ shouldCancelChannelMessage = false }, 5L, TimeUnit.SECONDS)
        } else if (shouldCancelChannelMessage && (event.plainMessage == LanguageData.ALREADY_IN_CHANNEL
                || (HytilsRebornConfig.chatSwapperHideAllChannelMsg && event.plainMessage.matches(LanguageData.CHANNEL_SWAP)))
        ) {
            event.cancelled = true
        }
    }

    private fun switchChattingTab(channel: String) {
        if (HytilsRebornConfig.chatSwapperChattingIntegration
            && FabricLoader.getInstance().isModLoaded("chatting")
            && ChattingConfig.chatTabs
        ) {
            val currentTabs = ChatTabs.currentTabs
            val tab = ChatTabs.tabs.find {
                val command = it.prefix ?: return@find false
                command.startsWith("/${channel.first()}c", ignoreCase = true)
                    || command.startsWith("/${channel.first()}chat", ignoreCase = true)
            }

            if (tab != null) {
                ChatTabs.currentTabs.clear()
                ChatTabs.currentTabs.add(tab)
                ChatTabs.refresh()

                Notifications.builder(
                    HytilsRebornConstants.NAME,
                    "Automatically switched to the ${tab.name} chat tab."
                ).type(NotificationType.INFO).action(NotificationAction("Switch back to previous tab", true) {
                    ChatTabs.currentTabs.clear()
                    ChatTabs.currentTabs.addAll(currentTabs)
                    ChatTabs.refresh()
                }).send()
            }
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.chatSwapper
}
