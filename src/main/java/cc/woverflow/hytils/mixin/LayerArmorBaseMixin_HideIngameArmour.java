/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin_HideIngameArmour {
    @Shadow
    public abstract ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot);

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorModel(I)Lnet/minecraft/client/model/ModelBase;"), cancellable = true)
    private void cancelArmor(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot, CallbackInfo ci) {
        if (shouldCancel(getCurrentArmor(entitylivingbaseIn, armorSlot))) {
            ci.cancel();
        }
    }

    private static boolean shouldCancel(ItemStack itemStack) {
        if (!HytilsConfig.hideArmor || itemStack == null || !HypixelUtils.INSTANCE.isHypixel()) return false;
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        final Item item = itemStack.getItem();
        if (locraw != null) {
            if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                switch (locraw.getGameType()) {
                    case BEDWARS:
                        return true;
                    case ARCADE_GAMES:
                        // capture the wool
                        return locraw.getGameMode().contains("PVP_CTW");
                    case DUELS:
                        return locraw.getGameMode().contains("BRIDGE") || locraw.getGameMode().contains("CAPTURE") || locraw.getGameMode().contains("ARENA");
                }
            } else {
                if (locraw.getGameType() == LocrawInfo.GameType.DUELS) {
                    return locraw.getGameMode().contains("CLASSIC") || locraw.getGameMode().contains("UHC");
                }
            }
        }

        return false;
    }
}
