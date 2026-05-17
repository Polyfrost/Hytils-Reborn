package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import org.polyfrost.hytils.client.features.game.HideArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
abstract class HumanoidArmorLayerMixin_HideIngameArmor {
    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
    private boolean shouldRenderArmorPiece(Equippable equippable, EquipmentSlot equipmentSlot, Operation<Boolean> original) {
        return original.call(equippable, equipmentSlot) && !HideArmor.shouldHideArmor(equippable);
    }
}
