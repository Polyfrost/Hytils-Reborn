package org.polyfrost.hytils.client.handlers.lobby

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.SoundPlayEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object SilentLobby {
    @Subscribe
    fun onSoundPlay(event: SoundPlayEvent) {
        if (!HytilsRebornConfig.isEnabled || HypixelUtils.getLocation().inGame()) return

        val path = event.sound.identifier.path
        if (HytilsRebornConfig.silentLobby && !path.startsWith("ui.")) {
            event.cancelled = true
        } else {
            if (DisableSoundRule.entries.any { it.shouldDisable(path) }) {
                event.cancelled = true
            }
        }
    }

    private enum class DisableSoundRule(
        val matches: (String) -> Boolean,
        val isEnabled: () -> Boolean
    ) {
        STEPPING({ it.endsWith(".step") }, { HytilsRebornConfig.lobbyDisableSteppingSounds }),
        SLIME({ it.startsWith("entity.slime") }, { HytilsRebornConfig.lobbyDisableSlimeSounds }),
        DRAGON({ it.startsWith("entity.ender_dragon") }, { HytilsRebornConfig.lobbyDisableDragonSounds }),
        WITHER({ it.startsWith("entity.wither") }, { HytilsRebornConfig.lobbyDisableWitherSounds }),
        ITEM_PICKUP({ it == "entity.item.pickup" }, { HytilsRebornConfig.lobbyDisableItemPickupSounds }),
        EXPERIENCE_ORB({ it == "entity.experience_orb.pickup" }, { HytilsRebornConfig.lobbyDisableExperienceOrbSounds }),
        PRIMED_TNT({ it == "entity.tnt.primed" }, { HytilsRebornConfig.lobbyDisablePrimedTntSounds }),
        EXPLOSION({ it == "entity.generic.explode" }, { HytilsRebornConfig.lobbyDisableExplosionSounds }),
        DELIVERY_MAN({ it == "entity.chicken.egg" }, { HytilsRebornConfig.lobbyDisableDeliveryManSounds }),
        NOTEBLOCK({ it.startsWith("block.note_block") }, { HytilsRebornConfig.lobbyDisableNoteBlockSounds }),
        FIREWORK({ it.startsWith("entity.firework_rocket") }, { HytilsRebornConfig.lobbyDisableFireworkSounds }),
        LEVEL_UP({ it == "entity.player.levelup" }, { HytilsRebornConfig.lobbyDisableLevelupSounds }),
        ARROW({ it.startsWith("entity.arrow") }, { HytilsRebornConfig.lobbyDisableArrowSounds }),
        BAT({ it.startsWith("entity.bat") }, { HytilsRebornConfig.lobbyDisableBatSounds }),
        FIRE({ it.startsWith("block.fire") }, { HytilsRebornConfig.lobbyDisableFireSounds }),
        ENDERMAN({ it.startsWith("entity.enderman") }, { HytilsRebornConfig.lobbyDisableEndermanSounds }),
        DOOR({ it.startsWithAny("block.wooden_door", "block.wooden_trapdoor", "block.iron_door", "block.iron_trapdoor") }, { HytilsRebornConfig.lobbyDisableDoorSounds }),
        PORTAL({ it.startsWith("block.portal") }, { HytilsRebornConfig.lobbyDisablePortalSounds });

        fun shouldDisable(path: String): Boolean = isEnabled() && matches(path)
    }

    private fun String.startsWithAny(vararg prefixes: String): Boolean {
        return prefixes.any { this.startsWith(it) }
    }
}
