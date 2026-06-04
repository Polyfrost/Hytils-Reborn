package org.polyfrost.hytils.mixin.client.events;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.polyfrost.hytils.client.events.SoundPlayEvent;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
abstract class SoundEngineMixin_SoundPlayEvent {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void preventLobbyMusic(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        SoundPlayEvent event = new SoundPlayEvent(instance);
        EventManager.INSTANCE.post(event);
        if (event.cancelled) {
            cir.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
        }
    }
}
