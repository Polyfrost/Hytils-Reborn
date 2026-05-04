package org.polyfrost.hytils.mixin.client.cosmetics;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hypixel.data.type.GameType;
//? if >=1.21.11 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
//?} else
//import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.client.data.providers.CosmeticsData;
import org.polyfrost.hytils.ducks.ItemClusterRenderStateDuck;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin_HideCosmetics {
    @Inject(method = /*? if >=1.21.11 {*/ "submit" /*?} else {*/ /*"render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V" *//*?}*/, at = @At("HEAD"), cancellable = true)
    public <S extends EntityRenderState> void hideCosmetics(/*? if >=1.21.11 {*/ S /*?} else {*/ /*EntityRenderState *//*?}*/ entityRenderState, /*? if >=1.21.11 {*/ CameraRenderState cameraRenderState, /*?}*/ double d, double e, double f, PoseStack poseStack, /*? if >=1.21.11 {*/ SubmitNodeCollector submitNodeCollector /*?} else {*/ /*MultiBufferSource multiBufferSource, int i, EntityRenderer<?, S> entityRenderer *//*?}*/, CallbackInfo ci) {
        if (!HytilsRebornConfig.isEnabled()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame()) return;

        GameType gameType = location.getGameType().orElse(null);
        if ((!HytilsRebornConfig.INSTANCE.getHideArcadeCosmetics() || gameType != GameType.ARCADE)
            && (!HytilsRebornConfig.INSTANCE.getHideDuelsCosmetics() || gameType != GameType.DUELS)
        ) return;

        if (!(entityRenderState instanceof ItemEntityRenderState itemEntityRenderState)) return;

        ItemStack itemStack = ((ItemClusterRenderStateDuck) itemEntityRenderState).hytils$getItemStack();
        if (itemStack != null && CosmeticsData.isItemCosmetic(itemStack.getItem().getDescriptionId())) {
            ci.cancel();
        }
    }
}
