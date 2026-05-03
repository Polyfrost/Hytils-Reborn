package org.polyfrost.hytils.mixin.client;

import net.hypixel.data.type.GameType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.hytils.HytilsRebornConstants;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin_NotifyMiningFatigue {
    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void onAddEffect(MobEffectInstance mobEffectInstance, Entity entity, CallbackInfo ci) {
        HypixelUtils.Location location = HypixelUtils.getLocation();

        //noinspection ConstantConditions
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getNotifyMiningFatigue()
            && (LivingEntity) (Object) this instanceof LocalPlayer
            && mobEffectInstance.getEffect() == MobEffects.MINING_FATIGUE
            && location.getGameType().orElse(null) != GameType.SMP
            && (!HytilsRebornConfig.INSTANCE.getDisableNotifyMiningFatigueSkyblock()
            || location.getGameType().orElse(null) != GameType.SKYBLOCK)
        ) {
            Minecraft mc = Minecraft.getInstance();
            mc.execute(() -> {
                switch (HytilsRebornConfig.INSTANCE.getMiningFatigueNotificationType()) {
                    case 0 -> Notifications.enqueue(
                        Notifications.Type.Warning,
                        HytilsRebornConstants.NAME,
                        "You have mining fatigue!"
                    );
                    case 1 -> {
                        mc.gui.setTitle(Component.literal("Mining Fatigue!").withStyle(ChatFormatting.RED));
                        mc.level.playPlayerSound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1, 1);
                    }
                    case 2 -> {
                        mc.level.addParticle(ParticleTypes.ELDER_GUARDIAN, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
                        mc.level.playPlayerSound(SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1, 1);
                    }
                }
            });
        }
    }
}
