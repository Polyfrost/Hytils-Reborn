package org.polyfrost.hytils.client.features.limbo

import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvents
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

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
            mc.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1f, 1f))
        }
    }
}
