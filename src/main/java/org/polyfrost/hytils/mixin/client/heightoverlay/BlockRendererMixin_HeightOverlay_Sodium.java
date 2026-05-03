package org.polyfrost.hytils.mixin.client.heightoverlay;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.game.HeightOverlay;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin_HeightOverlay_Sodium extends AbstractBlockRenderContext {
    @Dynamic
    @Inject(method = "tintQuad", at = @At("TAIL"))
    private void modifyBlockColor(MutableQuadViewImpl quad, CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getHeightOverlay() && HeightOverlay.shouldModify(this.state, this.pos)) {
            for (int i = 0; i < 4; i++) {
                quad.setColor(i, HeightOverlay.modifyColors(quad.baseColor(i), this.state));
            }
        }
    }
}
