/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.polyfrost.hytils.mixin;

import net.hypixel.data.type.GameType;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin_HideIngameArmour {
    @Shadow
    public abstract ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot);

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorModel(I)Lnet/minecraft/client/model/ModelBase;"), cancellable = true)
    private void hytils$cancelArmor(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot, CallbackInfo ci) {
        if (hytils$shouldCancel(getCurrentArmor(entitylivingbaseIn, armorSlot)) && !entitylivingbaseIn.isInvisible()) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean hytils$shouldCancel(ItemStack itemStack) {
        if (!HytilsConfig.hideArmor || itemStack == null || !HypixelUtils.isHypixel()) return false;
        final HypixelUtils.Location location = HypixelUtils.getLocation();
        final Item item = itemStack.getItem();
        if (location.getGameType().isPresent() && location.getMode().isPresent()) {
            if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                switch (location.getGameType().get()) {
                    case BEDWARS:
                        return true;
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
