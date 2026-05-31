package org.polyfrost.hytils.client.features.game

import dev.deftu.omnicore.api.client.sound.OmniClientSound
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniSounds
import net.hypixel.data.type.GameType
import net.minecraft.client.player.LocalPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object BlockCountNotifier {
    private var ticks = -1

    @Subscribe
    fun onTick(e: TickEvent.Start) {
        if (!HytilsRebornConfig.isEnabled || !HypixelUtils.isHypixel()) return
        val player: LocalPlayer = mc.player ?: return

        val equippedStack = player.mainHandItem
        if (equippedStack.count > HytilsRebornConfig.blockNumber || equippedStack.count <= 4) {
            if (ticks != -1) {
                ticks = -1
            }

            return
        }

        val equippedItem = equippedStack.item
        if (equippedItem == Items.TNT) {
            if (ticks != -1) {
                ticks = -1
            }

            return
        }

        val location = HypixelUtils.getLocation()
        if (HytilsRebornConfig.blockNotify && location.inGame() && location.gameType.isPresent) {
            when (location.gameType.get()) {
                GameType.BUILD_BATTLE, GameType.HOUSING, GameType.SKYBLOCK, GameType.SMP -> return
                else -> {}
            }

            ticks++
            if (ticks == 0) {
                playSound()
                return
            } else if (ticks == 20) {
                playSound()
                return
            }

            if (ticks > 40) {
                ticks = -1
            }
        }
    }

    fun playSound() {
        if (!mc.player!!.gameMode()!!.isSurvival) {
            return
        }

        val sound = when (HytilsRebornConfig.blockNotifySound) {
            0 -> OmniSounds.ENTITY.experienceOrb
            1 -> OmniSound.of(SoundEvents.IRON_GOLEM_HURT)
            2 -> OmniSound.of(SoundEvents.BLAZE_HURT)
            3 -> OmniSounds.BLOCK.anvilLand
            4 -> OmniSound.of(SoundEvents.HORSE_DEATH)
            5 -> OmniSound.of(SoundEvents.GHAST_SCREAM)
            6 -> OmniSound.of(SoundEvents.GUARDIAN_HURT_LAND)
            7 -> OmniSound.of(SoundEvents.CAT_AMBIENT)
            8 -> OmniSounds.WOLF.bark
            else -> null
        }

        if (sound != null) {
            OmniClientSound.play(sound, 1f, 1f)
        }
    }
}
