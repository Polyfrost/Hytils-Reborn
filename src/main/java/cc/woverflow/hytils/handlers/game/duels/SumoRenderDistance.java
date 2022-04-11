/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

package cc.woverflow.hytils.handlers.game.duels;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SumoRenderDistance {
    /*
    final GameSettings gs = Minecraft.getMinecraft().gameSettings;
    final int oldRd = gs.renderDistanceChunks;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (HytilsConfig.sumoRenderDistance && EssentialAPI.getMinecraftUtil().isHypixel() && (locraw != null && locraw.getGameMode().contains("SUMO"))) {
            gs.renderDistanceChunks = HytilsConfig.sumoRenderDistanceAmount;
        }
        else {
            gs.renderDistanceChunks = oldRd;
        }
    }
     */
}
