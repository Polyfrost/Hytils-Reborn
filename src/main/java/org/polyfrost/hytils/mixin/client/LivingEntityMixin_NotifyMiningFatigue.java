package org.polyfrost.hytils.mixin.client;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.hytils.client.features.game.NotifyMiningFatigue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin_NotifyMiningFatigue {
    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void onAddEffect(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        NotifyMiningFatigue.sendNotification((LivingEntity) (Object) this, effect.getEffect());
    }
}
