package org.polyfrost.hytils.mixin.client.hypixel;

import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HypixelUtils.Location.class)
abstract class LocationMixin_FixLobbyStatus {
    @Shadow public abstract Optional<String> getLobbyName();
    @Shadow public abstract Optional<String> getLastLobbyName();

    @Inject(method = "inLobby", at = @At("HEAD"), cancellable = true)
    private void fixInLobby(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(getLobbyName().isPresent());
    }

    @Inject(method = "wasInLobby", at = @At("HEAD"), cancellable = true)
    private void fixWasInLobby(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(getLastLobbyName().isPresent());
    }
}
