/*
 * Corgal, the all-in-one utility mod for Hypixel players.
 * Copyright (C) 2021 Corgal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
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

package club.sk1er.hytilities.mixin;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.cache.CosmeticsHandler;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.HypixelAPIUtils;
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
        if (Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() != null && Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation().getGameType() == GameType.DUELS && !HypixelAPIUtils.isLobby()) {
            String particleName = EnumParticleTypes.getParticleFromId(particleID).getParticleName();
            CosmeticsHandler.INSTANCE.particleCosmetics.forEach((particle) -> {
                if (particleName.equalsIgnoreCase(particle)) {
                    ci.cancel();
                }
            });
        }
    }
}
