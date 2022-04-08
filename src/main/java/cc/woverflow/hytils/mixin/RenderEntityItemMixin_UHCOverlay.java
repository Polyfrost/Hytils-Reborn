/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hytils.mixin;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.game.GameType;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
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
        LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (EssentialAPI.getMinecraftUtil().isHypixel() && locraw != null && (locraw.getGameType() == GameType.UHC_CHAMPIONS || locraw.getGameType() == GameType.SPEED_UHC) && HytilsConfig.uhcOverlay) {
            if (item.getRegistryName().equals("minecraft:apple") || item.getRegistryName().equals("minecraft:golden_apple") || item.getRegistryName().equals("minecraft:skull") || item.getRegistryName().equals("minecraft:gold_ingot")) {
                GlStateManager.scale(HytilsConfig.uhcOverlayMultiplier, HytilsConfig.uhcOverlayMultiplier, HytilsConfig.uhcOverlayMultiplier);
            }
        }
    }
}
