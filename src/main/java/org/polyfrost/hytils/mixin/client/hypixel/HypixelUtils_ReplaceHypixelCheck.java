package org.polyfrost.hytils.mixin.client.hypixel;

import org.polyfrost.hytils.client.utils.hypixel.HypixelModAPIImpl;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HypixelUtils.class)
abstract class HypixelUtils_ReplaceHypixelCheck {
    @Inject(method = "isHypixel", at = @At("HEAD"), cancellable = true)
    private static void replaceHypixelCheck(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(HypixelModAPIImpl.getOnHypixel());
    }
}
