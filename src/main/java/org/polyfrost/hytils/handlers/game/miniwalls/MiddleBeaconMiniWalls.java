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

package org.polyfrost.hytils.handlers.game.miniwalls;

import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;
import org.polyfrost.hytils.util.WaypointUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.events.PostWorldRenderEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldLoadEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class MiddleBeaconMiniWalls {
    private boolean miniWitherDead;
    private final BlockPos block = new BlockPos(0, 0, 0);

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        if (this.miniWitherDead) this.miniWitherDead = false;
    }

    @Subscribe
    public void onTitle(TitleEvent event) {
        final String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());
        if (unformattedTitle != null && (unformattedTitle.equals("Your Mini Wither died!") || unformattedTitle.equals("DEATHMATCH"))) miniWitherDead = true;
    }

    public boolean shouldMakeBeacon() {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        return HypixelUtils.isHypixel() && "mini_walls".equalsIgnoreCase(location.getMode().orElse(null)) && HytilsConfig.miniWallsMiddleBeacon && this.miniWitherDead;
    }

    @Subscribe
    public void onRenderWorldLast(PostWorldRenderEvent event) { // TODO
        if (!shouldMakeBeacon()) return;
        WaypointUtil.renderBeaconBeam(block, HytilsConfig.miniWallsMiddleBeaconColor, event.getPartialTicks());
    }
}
