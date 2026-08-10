package org.polyfrost.hytils.client.events

import net.minecraft.client.resources.sounds.SoundInstance
import org.polyfrost.oneconfig.api.event.v1.events.Event

// oneconfig SoundPlayedEvent cannot reliably cancel sounds on these versions so we use our own
data class SoundPlayEvent(val sound: SoundInstance) : Event.Cancellable()
