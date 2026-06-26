package org.polyfrost.hytils.mixin.client.heightoverlay;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
//~ if <1.21.11 'model' -> 'frapi.render'
import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
//~ if <1.21.11 'model' -> 'frapi.mesh'
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.features.game.HeightOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("MixinSuperClass") // no idea why it says this
@Mixin(BlockRenderer.class)
abstract class BlockRendererMixin_HeightOverlay_Sodium extends AbstractBlockRenderContext {
    @Inject(method = "tintQuad", at = @At("TAIL"))
    private void modifyBlockColor(MutableQuadViewImpl quad, CallbackInfo ci) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getHeightOverlay() && HeightOverlay.shouldModify(this.state, this.pos)) {
            for (int i = 0; i < 4; i++) {
                //~ if <1.21.11 '.setColor' -> '.color'
                quad.setColor(
                    i,
                    //~ if <1.21.11 '.baseColor' -> '.color'
                    HeightOverlay.modifyColors(quad.baseColor(i), this.state)
                );
            }
        }
    }
}
