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

package cc.woverflow.hytils.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderEntityItem.class)
public class RenderEntityItemMixin_UHCOverlay {
    @Inject(method = "func_177077_a(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I", at = @At(("TAIL")))
    public void scaleSpecialItems(EntityItem entity, double f4, double f5, double flag, float i, IBakedModel f, CallbackInfoReturnable<Integer> cir) {
        Item item = entity.getEntityItem().getItem();
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HypixelUtils.INSTANCE.isHypixel() && locraw != null && (locraw.getGameType() == LocrawInfo.GameType.UHC_CHAMPIONS || locraw.getGameType() == LocrawInfo.GameType.SPEED_UHC) && HytilsConfig.uhcOverlay) {
            if (item.getRegistryName().equals("minecraft:apple") || item.getRegistryName().equals("minecraft:golden_apple") || item.getRegistryName().equals("minecraft:skull") || item.getRegistryName().equals("minecraft:gold_ingot") || item.getRegistryName().equals("minecraft:gold_nugget")) {
                GlStateManager.scale(HytilsConfig.uhcOverlayMultiplier, HytilsConfig.uhcOverlayMultiplier, HytilsConfig.uhcOverlayMultiplier);
            }
        }
    }
}
