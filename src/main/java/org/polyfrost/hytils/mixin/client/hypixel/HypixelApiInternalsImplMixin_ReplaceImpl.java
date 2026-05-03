package org.polyfrost.hytils.mixin.client.hypixel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl;
import org.polyfrost.oneconfig.api.hypixel.v1.internal.HypixelApiInternalsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(HypixelApiInternalsImpl.class)
public class HypixelApiInternalsImplMixin_ReplaceImpl {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/polyfrost/oneconfig/api/hypixel/v1/internal/HypixelApiInternalsImpl;registerHypixelApi()V"))
    private static void registerHypixelModApiImpl(HypixelApiInternalsImpl instance, Operation<Void> original) {
        HypixelModAPIImpl.init();
    }
}
