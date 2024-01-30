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

package org.polyfrost.hytils.mixin.compat;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import club.sk1er.mods.levelhead.render.AboveHeadRender;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.polyfrost.hytils.config.HytilsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.polyfrost.hytils.handlers.game.pit.PitLagReducer.pitSpawnPos;

@Pseudo
@Mixin(value = AboveHeadRender.class, remap = false)
public class AboveHeadRendererMixin_RemoveLevelheadPit {
    @Inject(method = "render(Lnet/minecraftforge/client/event/RenderLivingEvent$Specials$Post;)V", at = @At("HEAD"), cancellable = true)
    private void removeLevelhead(RenderLivingEvent.Specials.Post<EntityLivingBase> event, CallbackInfo ci) {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            return;
        }

        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (locraw == null || locraw.getGameType() != LocrawInfo.GameType.PIT) {
            return;
        }

        if (HytilsConfig.pitLagReducer) {
            // If the entity being rendered is at spawn, and you are below spawn, cancel the rendering.
            if (event.entity.posY > pitSpawnPos && Minecraft.getMinecraft().thePlayer.posY < pitSpawnPos) {
                ci.cancel();
            }
        }
    }
}
