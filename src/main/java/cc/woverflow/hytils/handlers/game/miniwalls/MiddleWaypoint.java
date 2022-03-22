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


import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.TitleEvent;
import cc.woverflow.onecore.utils.RenderUtils;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiddleWaypoint {


    private boolean miniWitherDead;
    private String location;
    private Float x = 0f;
    private Float y = 2f;
    private Float z = 0f;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (this.miniWitherDead) {
            this.miniWitherDead = false;
        }
    }

    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        final String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());

        if (unformattedTitle != null && (unformattedTitle.equals("Your Mini Wither died!") ||
            unformattedTitle.equals("DEATHMATCH")) &&
            HytilsConfig.miniWallsMiddleWaypoint) {
            miniWitherDead = true;
        }
    }

    public boolean shouldMakeBeacon() {
        return this.miniWitherDead && HytilsConfig.miniWallsMiddleWaypoint;
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!HytilsConfig.miniWallsMiddleWaypoint) return;
        int rgb = 0xa839ce;
        if (miniWitherDead) return;{

        }
    }
}
