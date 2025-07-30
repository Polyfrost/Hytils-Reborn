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

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.polyfrost.hytils.handlers.lobby.tab.TabChanger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin_HideHeads {

    @Definition(id = "bl", local = @Local(type = boolean.class))
    @Expression("@(bl) != false")
    @ModifyExpressionValue(method = "renderPlayerlist", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean hytils$disablePlayerHead(boolean original, @Local(ordinal = 0) NetworkPlayerInfo networkPlayerInfo) {
        return original && TabChanger.shouldRenderPlayerHead(networkPlayerInfo);
    }

    @ModifyArgs(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawPing(IIILnet/minecraft/client/network/NetworkPlayerInfo;)V"))
    private void hytils$offsetPlayerHeadPing(Args args, @Local(ordinal = 0) boolean bl) {
        if (!TabChanger.shouldRenderPlayerHead(args.get(3)) && bl) {
            args.set(1, (int) args.get(1) + 9);
        }
    }
}
