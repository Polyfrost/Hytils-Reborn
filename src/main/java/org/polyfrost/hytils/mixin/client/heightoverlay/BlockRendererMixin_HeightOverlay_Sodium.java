package org.polyfrost.hytils.mixin.client.heightoverlay;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
//? if >=1.21.11 {
import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
//?} else {
/*import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
*///?}
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.game.HeightOverlay;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin_HeightOverlay_Sodium extends AbstractBlockRenderContext {
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
