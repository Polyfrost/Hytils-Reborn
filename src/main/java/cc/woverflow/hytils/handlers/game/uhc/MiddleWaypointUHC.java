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

package cc.woverflow.hytils.handlers.game.uhc;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.game.GameType;
import cc.woverflow.hytils.util.WaypointUtil;
import cc.woverflow.hytils.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiddleWaypointUHC {
    // y 70 is a completely arbitrary height, being slightly above sea level. feel free to change as one may see fit
    private final BlockPos block = new BlockPos(0,70,0);

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        LocrawInformation locraw = HytilsReborn.INSTANCE.getLocrawUtil().getLocrawInformation();
        if (EssentialAPI.getMinecraftUtil().isHypixel() && HytilsConfig.uhcMiddleWaypoint && (locraw != null && (locraw.getGameType() == GameType.UHC_CHAMPIONS || locraw.getGameType() == GameType.SPEED_UHC))) {
            WaypointUtil.renderWayPoint(HytilsConfig.uhcMiddleWaypointText, block, event.partialTicks);
        }
    }
}
