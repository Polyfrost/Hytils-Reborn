package org.polyfrost.hytils.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hypixel.data.type.GameType;
//? if >=1.21.11 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
//?} else {
/*import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
*///?}
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
public class EntityRenderDispatcherMixin_UHCOverlay {
    @Unique
    private final static Set<Item> uhcItems = Set.of(
        Items.APPLE,
        Items.GOLDEN_APPLE,
        Items.ENCHANTED_GOLDEN_APPLE,
        Items.PLAYER_HEAD,
        Items.GOLD_INGOT,
        Items.GOLD_BLOCK,
        Items.GOLD_NUGGET
    );

    //? if >=1.21.11 {
    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"))
    //?} else
    //@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    public <S extends EntityRenderState> void scaleUhcItems(/*? if >=1.21.11 {*/ S /*?} else {*/ /*EntityRenderState *//*?}*/ entityRenderState, /*? if >=1.21.11 {*/ CameraRenderState cameraRenderState, /*?}*/ double d, double e, double f, PoseStack poseStack, /*? if >=1.21.11 {*/ SubmitNodeCollector submitNodeCollector /*?} else {*/ /*MultiBufferSource multiBufferSource, int i, EntityRenderer<?, S> entityRenderer *//*?}*/, CallbackInfo ci) {
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
