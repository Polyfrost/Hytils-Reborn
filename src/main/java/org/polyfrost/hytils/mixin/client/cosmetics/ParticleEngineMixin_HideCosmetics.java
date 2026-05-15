package org.polyfrost.hytils.mixin.client.cosmetics;

import net.hypixel.data.type.GameType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.data.providers.CosmeticsData;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
abstract class ParticleEngineMixin_HideCosmetics {
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void hideCosmetics(ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i, CallbackInfoReturnable<Particle> cir) {
        if (!HytilsRebornConfig.isEnabled()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame()) return;

        GameType gameType = location.getGameType().orElse(null);
        if ((!HytilsRebornConfig.INSTANCE.getHideArcadeCosmetics() || gameType != GameType.ARCADE)
            && (!HytilsRebornConfig.INSTANCE.getHideDuelsCosmetics() || gameType != GameType.DUELS)
        ) return;

        Identifier particle = BuiltInRegistries.PARTICLE_TYPE.getKey(particleOptions.getType());
        if (particle != null && CosmeticsData.INSTANCE.getParticleCosmetics().contains(particle.getPath())) {
            cir.setReturnValue(null);
        }
    }
}
