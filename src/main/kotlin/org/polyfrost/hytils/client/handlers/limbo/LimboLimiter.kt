package org.polyfrost.hytils.client.handlers.limbo

import com.mojang.blaze3d.platform.FramerateLimitTracker
//? if >=1.21.11 {
import net.minecraft.util.Util
//?} else
//import net.minecraft.Util
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe

object LimboLimiter {
    var limboJoinTime = -1L

    @JvmStatic
    fun getThrottleReason(original: FramerateLimitTracker.FramerateThrottleReason): FramerateLimitTracker.FramerateThrottleReason {
        return if (limboJoinTime == -1L) {
            original
        } else if (Util.getMillis() - limboJoinTime > 600L * 1000L) {
            FramerateLimitTracker.FramerateThrottleReason.LONG_AFK
        } else if (Util.getMillis() - limboJoinTime > 5L * 1000L) {
            FramerateLimitTracker.FramerateThrottleReason.SHORT_AFK
        } else {
            original
        }
    }

    @Subscribe
    fun onLocationUpdate(event: HypixelLocationEvent) {
        limboJoinTime = if (event.location.serverName.orElse(null) == "limbo") {
            Util.getMillis()
        } else {
            -1L
        }
    }
}
