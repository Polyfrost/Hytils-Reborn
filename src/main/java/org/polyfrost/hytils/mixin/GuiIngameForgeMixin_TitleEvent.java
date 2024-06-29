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

import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.events.TitleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin_TitleEvent extends GuiIngame {
    public GuiIngameForgeMixin_TitleEvent(Minecraft mcIn) {
        super(mcIn);
    }

    @Redirect(method = "renderHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldInfo;isHardcoreModeEnabled()Z", remap = true))
    private boolean hytils$isHardcore(WorldInfo instance) {
        return instance.isHardcoreModeEnabled() || HytilsReborn.INSTANCE.getHardcoreStatus().shouldChangeStyle();
    }

    @Inject(method = "renderTitle", at = @At("HEAD"), cancellable = true)
    private void hytils$postTitleEvent(int l, int age, float opacity, CallbackInfo ci) {
        if (HypixelUtils.isHypixel()) {
            TitleEvent event = new TitleEvent(displayedTitle, displayedSubTitle);
            MinecraftForge.EVENT_BUS.post(event);

            // Set the title and subtitle to empty strings.
            if (event.isCanceled()) {
                displayTitle(null, null, -1, -1, -1);
                ci.cancel();
            }
        }
    }
}
