package org.polyfrost.hytils.mixin.client.hypixel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.HypixelLocationEvent;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.oneconfig.api.hypixel.v1.internal.HypixelApiInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(HypixelUtils.Location.class)
abstract class LocationMixin_PostLocationEvent {
    @WrapOperation(
        method = "onPacket(Lnet/hypixel/modapi/packet/impl/clientbound/event/ClientboundLocationPacket;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/polyfrost/oneconfig/api/hypixel/v1/internal/HypixelApiInternals;postLocationEvent()V"
        )
    )
    private void postLocationEvent(HypixelApiInternals instance, Operation<Void> original) {
        EventManager.INSTANCE.post(HypixelLocationEvent.INSTANCE);
    }
}
