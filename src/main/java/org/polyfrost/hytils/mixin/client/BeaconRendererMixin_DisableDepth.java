package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?} else
//import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.utils.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin_DisableDepth {
    //? if >=1.21.11 {
    @WrapOperation(method = "submitBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/resources/Identifier;FFIIIFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;beaconBeam(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    //?} else
    //@WrapOperation(method = "renderBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/resources/Identifier;FFJIIIFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;beaconBeam(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType disableBeaconBeamDepth(Identifier identifier, boolean isTranslucent, Operation<RenderType> original) {
        if (HytilsRebornConfig.isEnabled() && RenderUtils.beaconDisableDepth) {
            return RenderUtils.BEACON_BEAM_NO_DEPTH.apply(identifier, isTranslucent);
        } else {
            return /*? if >=1.21.11 {*/ RenderTypes /*?} else {*/ /*RenderType *//*?}*/.beaconBeam(identifier, isTranslucent);
        }
    }
}
