package org.polyfrost.hytils.mixin.client;

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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.hytils.ducks.ItemClusterRenderStateDuck;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(EntityRenderDispatcher.class)
abstract class EntityRenderDispatcherMixin_UHCOverlay {
    @Unique
    private final static Set<Item> uhcItems = Set.of(
        Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.PLAYER_HEAD, Items.GOLD_INGOT, Items.GOLD_BLOCK, Items.GOLD_NUGGET
    );

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
            value = "INVOKE",
            //? if >=1.21.10 {
            //~ if <26.1 'level/CameraRenderState' -> 'CameraRenderState'
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
            //?} else
            //target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
        )
    )
    public <S extends EntityRenderState> void scaleUhcItems(
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
        if (!HytilsRebornConfig.isEnabled() || !HytilsRebornConfig.INSTANCE.getUhcOverlay()) return;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!location.inGame()
            || location.getGameType().orElse(null) != GameType.UHC
            || location.getGameType().orElse(null) != GameType.SPEED_UHC
        ) return;

        if (!(entityRenderState instanceof ItemEntityRenderState itemEntityRenderState)
            || !uhcItems.contains(((ItemClusterRenderStateDuck) itemEntityRenderState).hytils$getItemStack().getItem())
        ) return;

        float scale = HytilsRebornConfig.INSTANCE.getUhcOverlayScale();
        poseStack.scale(scale, scale, scale);
    }
}
