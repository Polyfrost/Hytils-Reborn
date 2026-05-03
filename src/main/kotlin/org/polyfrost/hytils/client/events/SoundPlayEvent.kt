package org.polyfrost.hytils.client.events

import net.minecraft.client.resources.sounds.SoundInstance
import org.polyfrost.oneconfig.api.event.v1.events.Event

// We can't reliably cancel sounds using OneConfig's SoundPlayedEvent in 1.21(?), so we use our own
data class SoundPlayEvent(val sound: SoundInstance) : Event.Cancellable()
