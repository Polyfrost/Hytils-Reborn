package org.polyfrost.hytils.client.features.limbo

//~ if <1.21.11 'util.Util' -> 'Util'
import net.minecraft.util.Util
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import kotlin.math.min

object LimboLimiter {
    var limboJoinTime = -1L

    @JvmStatic
    fun getFramerateLimit(original: Int, framerateLimit: Int): Int {
        return if (limboJoinTime == -1L) {
            original
        } else if (Util.getMillis() - limboJoinTime > 600L * 1000L) {
            10 // matches the vanilla LONG_AFK throttle
        } else if (Util.getMillis() - limboJoinTime > 5L * 1000L) {
            min(framerateLimit, 30) // matches the vanilla SHORT_AFK throttle
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
