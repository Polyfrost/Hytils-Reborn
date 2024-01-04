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

package org.polyfrost.hytils.handlers.game.uhc;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.util.WaypointUtil;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiddleWaypointUHC {
    // y 70 is a completely arbitrary height, being slightly above sea level. feel free to change as one may see fit
    private final BlockPos block = new BlockPos(0,70,0);

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HypixelUtils.INSTANCE.isHypixel() && LocrawUtil.INSTANCE.isInGame() && HytilsConfig.uhcMiddleWaypoint && (locraw != null && (locraw.getGameType() == LocrawInfo.GameType.UHC_CHAMPIONS || locraw.getGameType() == LocrawInfo.GameType.SPEED_UHC))) {
            WaypointUtil.renderWayPoint(HytilsConfig.uhcMiddleWaypointText, block, event.partialTicks);
        }
    }
}
