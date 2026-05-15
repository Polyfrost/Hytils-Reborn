package org.polyfrost.hytils.client.handlers.chat.modules.triggers

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object BroadcastAchievement : ChatReceiveModule {
    private val achievements = HashSet<String>()

    override fun onChatReceived(event: ChatReceiveEvent) {
        LanguageData.ACHIEVEMENT_UNLOCKED.find(event.unformattedMessage)?.let { match ->
            val achievement = match.groups["achievement"]?.value ?: return
            achievements.add(achievement)
            OmniClientChatSender.queue("/gc Achievement unlocked! I unlocked the $achievement achievement!")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.broadcastAchievements
}
