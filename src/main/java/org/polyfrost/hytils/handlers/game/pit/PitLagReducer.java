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

package org.polyfrost.hytils.handlers.game.pit;

import net.hypixel.data.type.GameType;
import net.minecraft.entity.Entity;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import org.polyfrost.oneconfig.api.event.v1.events.RenderLivingEntityEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldLoadEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class PitLagReducer {

    /** The y-position of the spawn platform in The Pit. */
    private double pitSpawnPos;

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        // Allow the spawn position to be updated.
        pitSpawnPos = -1;
    }

    @Subscribe(priority = 5)
    public void onRenderLiving(RenderLivingEntityEvent.Pre event) { // TODO
        if (!HypixelUtils.isHypixel()) {
            return;
        }

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (location.getGameType().orElse(null) != GameType.PIT) {
            return;
        }

        Entity entity = (Entity) event.getEntity();
        if (!(entity instanceof EntityLiving)) {
            return;
        }

        if (pitSpawnPos == -1) {
            // Update the spawn position by finding an armor stand in spawn.
            if (entity instanceof EntityArmorStand && entity.getName().equals("§a§lJUMP! §c§lFIGHT!")) {
                pitSpawnPos = entity.posY - 5;
            }
        } else if (HytilsConfig.pitLagReducer) {
            // If the entity being rendered is at spawn, and you are below spawn, cancel the rendering.
            if (entity.posY > pitSpawnPos && Minecraft.getMinecraft().thePlayer.posY < pitSpawnPos) {
                event.cancelled = true;
            }
        }
    }
}
