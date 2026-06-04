package org.polyfrost.hytils.mixin.client.events;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.polyfrost.hytils.client.events.TitleEvent;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin_TitleEvent {
    @Shadow private Component title;
    @Shadow private Component subtitle;
    @Shadow private int titleTime;

    //~ if <26.1 'extractTitle' -> 'renderTitle'
    @Inject(method = "extractTitle", at = @At("HEAD"), cancellable = true)
    private void onRenderTitle(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.title == null || titleTime <= 0) return;

        TitleEvent event = new TitleEvent(this.title, this.subtitle);
        EventManager.INSTANCE.post(event);
        if (event.cancelled) {
            ci.cancel();
        }
    }
}
