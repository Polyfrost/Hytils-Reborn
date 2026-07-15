package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.polyfrost.hytils.client.features.game.HideArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidMobRenderer.class)
abstract class HumanoidMobRendererMixin_HideIngameArmor {
    @ModifyExpressionValue(
        method = "getEquipmentIfRenderable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
        )
    )
    private static boolean shouldRenderArmorPiece(boolean shouldRender, @Local ItemStack itemStack) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return shouldRender && !HideArmor.shouldHideArmor(equippable);
    }
}
