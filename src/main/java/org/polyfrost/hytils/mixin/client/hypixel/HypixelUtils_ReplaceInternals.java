package org.polyfrost.hytils.mixin.client.hypixel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;

@Mixin(HypixelUtils.class)
abstract class HypixelUtils_ReplaceInternals {
    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
        )
    )
    private static Object replaceInternals(Iterator<?> iterator, Operation<Boolean> operation) {
        return null;
    }
}
