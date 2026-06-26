package org.polyfrost.hytils.client.features.game

import net.hypixel.data.type.GameType
import net.minecraft.ChatFormatting
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.api.notifications.v1.Notifications
import org.polyfrost.oneconfig.utils.v1.dsl.mc

object NotifyMiningFatigue {
    @JvmStatic
    fun sendNotification(livingEntity: LivingEntity, mobEffect: Holder<MobEffect>) {
        if (!HytilsRebornConfig.isEnabled || !HytilsRebornConfig.notifyMiningFatigue || !HypixelUtils.isHypixel()) return

        if (livingEntity !is LocalPlayer || mobEffect != MobEffects.MINING_FATIGUE) return

        val gameType = HypixelUtils.getLocation().gameType.orElse(null) ?: return
        if ((HytilsRebornConfig.disableNotifyMiningFatigueSkyblock && gameType == GameType.SKYBLOCK)
            || gameType == GameType.SMP
        ) return

        mc.execute {
            when (HytilsRebornConfig.miningFatigueNotificationType) {
                0 -> Notifications.info(HytilsRebornConstants.NAME, "You have mining fatigue!")

                1 -> {
                    //~ if <26.2 'gui.hud' -> 'gui'
                    mc.gui.hud.setTitle(Component.literal("Mining Fatigue!").withStyle(ChatFormatting.RED))
                    mc.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1f, 1f))
                }

                2 -> {
                    mc.level!!.addParticle(
                        ParticleTypes.ELDER_GUARDIAN,
                        mc.player!!.x,
                        mc.player!!.y,
                        mc.player!!.z,
                        0.0,
                        0.0,
                        0.0
                    )
                    mc.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.ELDER_GUARDIAN_CURSE, 1f, 1f))
                }
            }
        }
    }
}
