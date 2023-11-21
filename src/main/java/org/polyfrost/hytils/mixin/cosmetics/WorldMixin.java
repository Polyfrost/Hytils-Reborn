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

import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.CosmeticsHandler;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "spawnParticle(IZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    private void removeParticles(int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int[] p_175720_15_, CallbackInfo ci) {
        if ((HytilsConfig.hideDuelsCosmetics && LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.DUELS) ||
            (HytilsConfig.hideArcadeCosmetics && LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType() == LocrawInfo.GameType.ARCADE_GAMES) && LocrawUtil.INSTANCE.isInGame()) {
            String particleName = EnumParticleTypes.getParticleFromId(particleID).getParticleName();
            CosmeticsHandler.INSTANCE.particleCosmetics.forEach((particle) -> {
                if (particleName.equalsIgnoreCase(particle)) {
                    ci.cancel();
                }
            });
        }
    }
}
