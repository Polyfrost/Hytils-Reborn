package org.polyfrost.hytils.mixin.client.cosmetics;

//? if >=1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//~ if <26.1 'level.CameraRenderState' -> 'CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
*///?}

import com.mojang.blaze3d.vertex.PoseStack;
import net.hypixel.data.type.GameType;
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
abstract class EntityRenderDispatcherMixin_HideCosmetics {
    @Inject(
        method = {
            //? if >=1.21.10 {
            "submit",
            //?} else if >=1.21.5 {
            /*"render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V"
            *///?} else
            //"render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V"
        },
        at = @At(
            //? if >=1.21.5 {
            "HEAD"
            //?} else {
            /*value = "INVOKE",
            // we inject here so we can capture the entityRenderState local variable
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getRenderOffset(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/world/phys/Vec3;"
            *///?}
        ),
        cancellable = true
    )
    public <S extends EntityRenderState> void hideCosmetics(
        //? if >=1.21.10 {
        S entityRenderState,
        //?} else if >=1.21.5 {
        /*EntityRenderState entityRenderState,
        *///?} else
        //net.minecraft.world.entity.Entity entity,
        //? if >=1.21.10
        CameraRenderState camera,
        double x,
        double y,
        double z,
        //? if <1.21.5
        //float g,
        PoseStack poseStack,
        //? if >=1.21.10 {
        SubmitNodeCollector submitNodeCollector,
        //?} else {
        /*MultiBufferSource multiBufferSource,
        int i,
        EntityRenderer<?, S> entityRenderer,
        *///?}
        //? if >=1.21.5 {
        CallbackInfo ci
        //?} else {
        /*CallbackInfo ci,
        @com.llamalad7.mixinextras.sugar.Local S entityRenderState
        *///?}
    ) {
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
