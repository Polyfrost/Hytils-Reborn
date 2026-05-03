package org.polyfrost.hytils.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import org.polyfrost.hytils.client.utils.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin_CleanUp {
    @Inject(method = "close", at = @At("RETURN"))
    private void onClose(CallbackInfo ci) {
        RenderUtils.close();
    }
}
