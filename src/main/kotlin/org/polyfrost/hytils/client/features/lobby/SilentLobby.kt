package org.polyfrost.hytils.client.features.lobby

import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.events.SoundPlayEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object SilentLobby {
    @Subscribe
    fun onSoundPlay(event: SoundPlayEvent) {
        if (!HytilsRebornConfig.isEnabled || HypixelUtils.getLocation().inGame()) return

        //~ if <1.21.11 '.identifier' -> '.location'
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
        STEPPING({ it.endsWith(".step") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableSteppingSounds }),
        SLIME({ it.startsWith("entity.slime") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableSlimeSounds }),
        DRAGON({ it.startsWith("entity.ender_dragon") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableDragonSounds }),
        WITHER({ it.startsWith("entity.wither") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableWitherSounds }),
        ITEM_PICKUP({ it == "entity.item.pickup" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableItemPickupSounds }),
        EXPERIENCE_ORB({ it == "entity.experience_orb.pickup" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableExperienceOrbSounds }),
        PRIMED_TNT({ it == "entity.tnt.primed" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisablePrimedTntSounds }),
        EXPLOSION({ it == "entity.generic.explode" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableExplosionSounds }),
        DELIVERY_MAN({ it == "entity.chicken.egg" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableDeliveryManSounds }),
        NOTEBLOCK({ it.startsWith("block.note_block") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableNoteBlockSounds }),
        FIREWORK({ it.startsWith("entity.firework_rocket") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableFireworkSounds }),
        LEVEL_UP({ it == "entity.player.levelup" }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableLevelupSounds }),
        ARROW({ it.startsWith("entity.arrow") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableArrowSounds }),
        BAT({ it.startsWith("entity.bat") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableBatSounds }),
        FIRE({ it.startsWith("block.fire") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableFireSounds }),
        ENDERMAN({ it.startsWith("entity.enderman") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableEndermanSounds }),
        DOOR({ it.startsWithAny("block.wooden_door", "block.wooden_trapdoor", "block.iron_door", "block.iron_trapdoor") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisableDoorSounds }),
        PORTAL({ it.startsWith("block.portal") }, { HytilsRebornConfig.DisableSpecificLobbySounds.lobbyDisablePortalSounds });

        fun shouldDisable(path: String): Boolean = isEnabled() && matches(path)
    }

    private fun String.startsWithAny(vararg prefixes: String): Boolean {
        return prefixes.any { this.startsWith(it) }
    }
}
