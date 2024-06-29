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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;

public class PitLagReducer {

    /** The y-position of the spawn platform in The Pit. */
    private double pitSpawnPos;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // Allow the spawn position to be updated.
        pitSpawnPos = -1;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderLiving(RenderLivingEvent.Pre<EntityLiving> event) {
        if (!HypixelUtils.isHypixel()) {
            return;
        }

        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (location.getGameType().orElse(null) != GameType.PIT) {
            return;
        }

        if (pitSpawnPos == -1) {
            // Update the spawn position by finding an armor stand in spawn.
            if (event.entity instanceof EntityArmorStand && event.entity.getName().equals("§a§lJUMP! §c§lFIGHT!")) {
                pitSpawnPos = event.entity.posY - 5;
            }
        } else if (HytilsConfig.pitLagReducer) {
            // If the entity being rendered is at spawn, and you are below spawn, cancel the rendering.
            if (event.entity.posY > pitSpawnPos && Minecraft.getMinecraft().thePlayer.posY < pitSpawnPos) {
                event.setCanceled(true);
            }
        }
    }
}
