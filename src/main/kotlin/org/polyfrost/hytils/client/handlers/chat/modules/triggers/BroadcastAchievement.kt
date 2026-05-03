package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule
import org.polyfrost.hytils.client.data.providers.LanguageData

object BroadcastAchievement : ChatReceiveModule {
    private val achievements = HashSet<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return
        LanguageData.ACHIEVEMENT_UNLOCKED.find(event.unformattedMessage)?.let { match ->
            val achievement = match.groups["achievement"]?.value ?: return
            achievements.add(achievement)
            OmniClientChatSender.queue("/gc Achievement unlocked! I unlocked the $achievement achievement!")
        }
    }

    override fun isEnabled() = HytilsRebornConfig.broadcastAchievements
}
