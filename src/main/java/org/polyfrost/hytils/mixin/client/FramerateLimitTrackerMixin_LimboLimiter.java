package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.limbo.LimboLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FramerateLimitTracker.class)
abstract class FramerateLimitTrackerMixin_LimboLimiter {
    @Shadow private int framerateLimit;

    @ModifyReturnValue(method = "getFramerateLimit", at = @At("RETURN"))
    private int limitLimboFramerate(int original) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getLimboLimiter()) {
            return LimboLimiter.getFramerateLimit(original, this.framerateLimit);
        }

        return original;
    }
}
