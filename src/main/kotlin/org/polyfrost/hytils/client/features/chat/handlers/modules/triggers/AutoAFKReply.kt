package org.polyfrost.hytils.client.features.chat.handlers.modules.triggers

//? if >=1.21.11 {
import net.minecraft.util.Util
//?} else
//import net.minecraft.Util

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.features.chat.handlers.ChatReceiveModule
import org.polyfrost.hytils.mixin.client.accessor.FramerateLimitTrackerAccessor
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object AutoAFKReply : ChatReceiveModule {
    override fun onChatReceived(event: ChatReceiveEvent) {
        val latestInputTime = (mc.framerateLimitTracker as FramerateLimitTrackerAccessor).latestInputTime
        if (Util.getMillis() - latestInputTime < HytilsRebornConfig.afkTimeout * 60L * 1000L) return

        LanguageData.PRIVATE_MESSAGE.find(event.unformattedMessage)?.let { match ->
            val type = match.groups["type"]?.value ?: return
            if (type != "From") return

            val player = match.groups["player"]?.value ?: return
            val message = HytilsRebornConfig.afkReplyMessage.replace("%player%", player)
            mc.player?.connection?.sendChat("/msg $player $message")
        }
    }

    override val isEnabled
        get() = HytilsRebornConfig.autoReplyAfk
}
