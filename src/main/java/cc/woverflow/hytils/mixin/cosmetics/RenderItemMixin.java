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

package cc.woverflow.hytils.mixin.cosmetics;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.cache.CosmeticsHandler;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.*;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", at = @At("HEAD"), cancellable = true)
    private void yeah(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (stack == null) return;
        if ((HytilsConfig.hideDuelsCosmetics && LocrawUtil.INSTANCE.getLocrawInfo() != null &&
            LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.DUELS) || (HytilsConfig.hideArcadeCosmetics && LocrawUtil.INSTANCE.getLocrawInfo() != null &&
            LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.ARCADE_GAMES) && !HytilsReborn.INSTANCE.getLobbyChecker().playerIsInLobby() &&
            (stack.getItem() instanceof ItemDoublePlant || stack.getItem() instanceof ItemDye || stack.getItem() instanceof ItemRecord || shouldRemove(stack.getItem().getUnlocalizedName()) ||(stack.getItem() instanceof ItemBlock && (shouldRemove(((ItemBlock) stack.getItem()).block.getUnlocalizedName()) || ((ItemBlock) stack.getItem()).block instanceof BlockPumpkin)))) ci.cancel();
    }

    private boolean shouldRemove(String name) {
        AtomicBoolean yes = new AtomicBoolean();
        CosmeticsHandler.INSTANCE.itemCosmetics.forEach((itemName) -> {
            if (name.equals(itemName)) yes.set(true);
        });
        return yes.get();
    }
}
