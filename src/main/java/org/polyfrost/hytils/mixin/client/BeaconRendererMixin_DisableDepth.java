package org.polyfrost.hytils.mixin.client;

//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?} else
//import net.minecraft.client.renderer.RenderType;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.Identifier;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.utils.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconRenderer.class)
abstract class BeaconRendererMixin_DisableDepth {
    @WrapOperation(
        //? if >=1.21.11 {
        method = "submitBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/resources/Identifier;FFIIIFF)V",
        //?} else
        //method = "renderBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/resources/Identifier;FFJIIIFF)V",
        at = @At(
            value = "INVOKE",
            //? if >=1.21.11 {
            target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;beaconBeam(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/rendertype/RenderType;"
            //?} else
            //target = "Lnet/minecraft/client/renderer/RenderType;beaconBeam(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/RenderType;"
        )
    )
    private static RenderType disableBeaconBeamDepth(Identifier identifier, boolean isTranslucent, Operation<RenderType> original) {
        if (HytilsRebornConfig.isEnabled() && RenderUtils.beaconDisableDepth) {
            return RenderUtils.BEACON_BEAM_NO_DEPTH.apply(identifier, isTranslucent);
        }

        //~ if <1.21.11 'RenderTypes' -> 'RenderType'
        return RenderTypes.beaconBeam(identifier, isTranslucent);
    }
}
