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
import net.minecraftforge.client.GuiIngameForge;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false, priority = 990)
public class GuiIngameForgeMixin_HideActionbar {
    @Inject(method = "renderRecordOverlay", at = @At("HEAD"), cancellable = true)
    private void hytils$cancelActionBar(int width, int height, float partialTicks, CallbackInfo ci) {
        if (HypixelUtils.isHypixel() && ((HytilsConfig.hideHousingActionBar && HypixelUtils.getLocation().getGameType().orElse(null) == GameType.HOUSING) || (HytilsConfig.hideDropperActionBar && "DROPPER".equals(HypixelUtils.getLocation().getMode().orElse(null)))))
            ci.cancel();
    }
}
