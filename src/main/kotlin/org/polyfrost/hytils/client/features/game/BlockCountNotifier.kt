package org.polyfrost.hytils.client.features.game

import net.hypixel.data.type.GameType
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvents
//? if >=26.1
import net.minecraft.world.entity.animal.feline.CatSoundVariants
//? if >=1.21.5
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants
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
        //~ if <1.21.5 'gameMode()!!' -> 'connection.getPlayerInfo(mc.player!!.uuid)!!.gameMode'
        if (!mc.player!!.gameMode()!!.isSurvival) return

        val sound = when (HytilsRebornConfig.blockNotifySound) {
            0 -> SoundEvents.EXPERIENCE_ORB_PICKUP
            1 -> SoundEvents.IRON_GOLEM_HURT
            2 -> SoundEvents.BLAZE_HURT
            3 -> SoundEvents.ANVIL_LAND
            4 -> SoundEvents.HORSE_DEATH
            5 -> SoundEvents.GHAST_SCREAM
            6 -> SoundEvents.GUARDIAN_HURT_LAND
            //? if >=26.1 {
            7 -> SoundEvents.CAT_SOUNDS[CatSoundVariants.SoundSet.CLASSIC]?.adultSounds?.ambientSound?.value()
            //?} else
            //7 -> SoundEvents.CAT_AMBIENT
            //? if >=1.21.5 {
            8 -> SoundEvents.WOLF_SOUNDS[WolfSoundVariants.SoundSet.CLASSIC]?./*? if >=26.1 {*/ adultSounds?. /*?}*/ambientSound?.value()
            //?} else
            //8 -> SoundEvents.WOLF_AMBIENT
            else -> null
        }

        if (sound != null) {
            mc.soundManager.play(SimpleSoundInstance.forUI(sound, 1f, 1f))
        }
    }
}
