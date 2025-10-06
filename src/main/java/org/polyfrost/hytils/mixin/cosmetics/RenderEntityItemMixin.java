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

package org.polyfrost.hytils.mixin.cosmetics;

import net.hypixel.data.type.GameType;
import net.minecraft.block.Block;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.CosmeticsHandler;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.*;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(RenderEntityItem.class)
public class RenderEntityItemMixin {
    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void hytils$removeItemCosmetics(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        ItemStack stack = entity.getEntityItem();
        if (stack == null) {
            return;
        }

        final HypixelUtils.Location loc = HypixelUtils.getLocation();
        final GameType gt = loc.getGameType().orElse(null);
        if (gt == null) {
            return;
        }

        final boolean modeEnabled =
            (HytilsConfig.hideDuelsCosmetics && gt == GameType.DUELS) ||
            (HytilsConfig.hideArcadeCosmetics && gt == GameType.ARCADE);
        if (!modeEnabled || !loc.inGame()) {
            return;
        }

        final Item item = stack.getItem();
        boolean hiding = item instanceof ItemDye ||
            item instanceof ItemRecord ||
            hytils$shouldRemove(item.getUnlocalizedName());
        if (!hiding && item instanceof ItemBlock) {
            final Block block = ((ItemBlock) item).getBlock();
            hiding = hytils$shouldRemove(block.getUnlocalizedName()) || block instanceof BlockPumpkin;
        }

        if (hiding) {
            ci.cancel();
        }
    }

    @Unique
    private boolean hytils$shouldRemove(String name) {
        AtomicBoolean yes = new AtomicBoolean();
        CosmeticsHandler.INSTANCE.itemCosmetics.forEach((itemName) -> {
            if (name.equals(itemName)) yes.set(true);
        });
        return yes.get();
    }
}
