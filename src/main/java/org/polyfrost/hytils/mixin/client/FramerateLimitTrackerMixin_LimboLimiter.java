package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.limbo.LimboLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FramerateLimitTracker.class)
abstract class FramerateLimitTrackerMixin_LimboLimiter {
    @ModifyReturnValue(method = "getThrottleReason", at = @At("RETURN"))
    private FramerateLimitTracker.FramerateThrottleReason limitLimboFramerate(FramerateLimitTracker.FramerateThrottleReason original) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getLimboLimiter()) {
            return LimboLimiter.getThrottleReason(original);
        }

        return original;
    }
}
