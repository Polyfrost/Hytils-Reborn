package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.client.utils.ChatUtils

object BroadcastAchievement : ChatReceiveModule {
    private val achievements = mutableSetOf<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        LanguageData.ACHIEVEMENT_UNLOCKED.find(event.unformattedMessage)?.let { match ->
            val achievement = match.groups["achievement"]?.value ?: return
            achievements.add(achievement)
            ChatUtils.queueMessage("/gc Achievement unlocked! I unlocked the $achievement achievement!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.broadcastAchievements
}
