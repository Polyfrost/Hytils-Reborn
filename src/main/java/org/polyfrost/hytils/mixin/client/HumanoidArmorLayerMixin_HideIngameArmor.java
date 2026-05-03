package org.polyfrost.hytils.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hypixel.data.type.GameType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import org.polyfrost.hytils.client.HytilsRebornConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin_HideIngameArmor {
    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
    private boolean shouldRenderArmorPiece(Equippable equippable, EquipmentSlot equipmentSlot, Operation<Boolean> original) {
        return original.call(equippable, equipmentSlot) && !shouldHideArmor(equippable);
    }

    @Unique
    private boolean shouldHideArmor(Equippable equippable) {
        if (!HytilsRebornConfig.isEnabled()
            || !HytilsRebornConfig.INSTANCE.getHideArmor()
            || equippable.assetId().isEmpty()
            || !HypixelUtils.isHypixel()
        ) return false;

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (location.getGameType().isPresent() && location.getMode().isPresent()) {
            if (equippable.assetId().get().identifier().getPath().startsWith("leather")) {
                switch (location.getGameType().get()) {
                    case BEDWARS:
                        return !location.getMode().get().contains("LUCKY");
                    case ARCADE:
                        // capture the wool
                        return location.getMode().get().contains("PVP_CTW");
                    case DUELS:
                        return location.getMode().get().contains("BRIDGE") || location.getMode().get().contains("CAPTURE") || location.getMode().get().contains("ARENA");
                }
            } else {
                if (location.getGameType().get() == GameType.DUELS) {
                    return location.getMode().get().contains("CLASSIC") || location.getMode().get().contains("UHC");
                }
            }
        }

        return false;
    }
}
