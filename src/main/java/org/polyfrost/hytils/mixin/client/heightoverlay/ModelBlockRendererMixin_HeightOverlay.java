package org.polyfrost.hytils.mixin.client.heightoverlay;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.handlers.game.HeightOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin_HeightOverlay {
    @Inject(method = "putQuadData", at = @At("HEAD"))
    private void checkShouldModify(CallbackInfo ci, @Local(argsOnly = true) BlockState blockState, @Local(argsOnly = true) BlockPos blockPos, @Share("shouldModify") LocalBooleanRef shouldModifyRef) {
        if (HytilsRebornConfig.isEnabled() && HytilsRebornConfig.INSTANCE.getHeightOverlay()) {
            shouldModifyRef.set(HeightOverlay.shouldModify(blockState, blockPos));
        } else {
            shouldModifyRef.set(false);
        }
    }

    //~ if <1.21.11 '[FFFFF[II)V' -> '[FFFFF[IIZ)V' {
    @ModifyArg(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[II)V"), index = 3)
    private float modifyBlockColor$Red(float red, @Local(argsOnly = true) BlockState blockState, @Share("shouldModify") LocalBooleanRef shouldModifyRef) {
        if (shouldModifyRef.get()) {
            int modified = HeightOverlay.modifyColors((int) (red * 255F), blockState);
            return (modified & 0xFF) / 255.0F;
        } else {
            return red;
        }
    }

    @ModifyArg(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[II)V"), index = 4)
    private float modifyBlockColor$Green(float green, @Local(argsOnly = true) BlockState blockState, @Share("shouldModify") LocalBooleanRef shouldModifyRef) {
        if (shouldModifyRef.get()) {
            int modified = HeightOverlay.modifyColors(((int) (green * 255F)) << 8, blockState);
            return ((modified >> 8) & 0xFF) / 255F;
        } else {
            return green;
        }
    }

    @ModifyArg(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[II)V"), index = 5)
    private float modifyBlockColor$Blue(float blue, @Local(argsOnly = true) BlockState blockState, @Share("shouldModify") LocalBooleanRef shouldModifyRef) {
        if (shouldModifyRef.get()) {
            int modified = HeightOverlay.modifyColors(((int) (blue * 255F)) << 16, blockState);
            return ((modified >> 16) & 0xFF) / 255F;
        } else {
            return blue;
        }
    }
    //~}
}
