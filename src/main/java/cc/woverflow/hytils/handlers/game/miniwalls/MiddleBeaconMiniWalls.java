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

package cc.woverflow.hytils.handlers.game.miniwalls;


import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.TitleEvent;
import cc.woverflow.hytils.util.WaypointUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiddleBeaconMiniWalls {
    private boolean miniWitherDead;
    private final BlockPos block = new BlockPos(0, 0, 0);

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (this.miniWitherDead) this.miniWitherDead = false;
    }

    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        final String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());
        if (unformattedTitle != null && (unformattedTitle.equals("Your Mini Wither died!") || unformattedTitle.equals("DEATHMATCH"))) miniWitherDead = true;
    }

    public boolean shouldMakeBeacon() {
        LocrawInfo locraw = HypixelUtils.INSTANCE.getLocrawInfo();
        return HypixelUtils.INSTANCE.isHypixel() && locraw != null && locraw.getGameMode().equalsIgnoreCase("mini_walls") && HytilsConfig.miniWallsMiddleBeacon && this.miniWitherDead;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!shouldMakeBeacon()) return;
        WaypointUtil.renderBeaconBeam(block, HytilsConfig.miniWallsMiddleBeaconColor.getRGB(), 1.0f, event.partialTicks);
    }
}
