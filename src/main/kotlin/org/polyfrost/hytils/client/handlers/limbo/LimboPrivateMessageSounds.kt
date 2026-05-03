package org.polyfrost.hytils.client.handlers.limbo

import dev.deftu.omnicore.api.client.sound.OmniClientSound
import dev.deftu.omnicore.api.sound.OmniSounds
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

// FIXME: this works with short pm channels but it might just be a race condition
//  if [ChatHandler] is registered after this, it seems to not work
object LimboPrivateMessageSounds {
    @Subscribe
    fun onChatReceived(event: ChatReceiveEvent) {
        if (!HytilsRebornConfig.isEnabled
            || !HytilsRebornConfig.limboPmDing
            || event.isOverlay
            || HypixelUtils.getLocation().serverName.orElse(null) != "limbo"
        ) return

        LanguageData.PRIVATE_MESSAGE.find(event.unformattedMessage)?.let { match ->
            val type = match.groups["type"]?.value ?: return
            if (type != "From") return
            OmniClientSound.play(OmniSounds.ENTITY.experienceOrb, 1f, 1f)
        }
    }
}
