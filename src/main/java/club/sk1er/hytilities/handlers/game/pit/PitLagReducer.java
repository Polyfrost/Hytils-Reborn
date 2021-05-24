/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.handlers.game.pit;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PitLagReducer {

    /** The y-position of the spawn platform in The Pit. */
    private double pitSpawnPos;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // Allow the spawn position to be updated.
        pitSpawnPos = -1;
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<EntityLiving> event) {
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) {
            return;
        }

        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (locraw == null || locraw.getGameType() != GameType.PIT) {
            return;
        }

        if (pitSpawnPos == -1) {
            // Update the spawn position by finding an armor stand in spawn.
            if (event.entity instanceof EntityArmorStand && event.entity.getName().equals("§a§lJUMP! §c§lFIGHT!")) {
                pitSpawnPos = event.entity.posY - 5;
            }
        } else if (HytilitiesConfig.pitLagReducer) {
            // If the entity being rendered is at spawn, and you are below spawn, cancel the rendering.
            if (event.entity.posY > pitSpawnPos && Minecraft.getMinecraft().thePlayer.posY < pitSpawnPos) {
                event.setCanceled(true);
            }
        }
    }
}
