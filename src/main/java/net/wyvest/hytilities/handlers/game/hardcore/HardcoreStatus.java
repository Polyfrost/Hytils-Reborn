/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.game.hardcore;

import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.events.TitleEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HardcoreStatus {

    private boolean danger;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (this.danger) {
            this.danger = false;
        }
    }

    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        final String unformattedTitle = EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle());

        if (unformattedTitle != null && (unformattedTitle.equals("Your Mini Wither died!") ||
            unformattedTitle.equals("Your Wither died!") ||
            unformattedTitle.equals("BED DESTROYED!")) &&
            HytilitiesConfig.hardcoreHearts) {
            danger = true;
        }
    }

    public boolean shouldChangeStyle() {
        return this.danger && HytilitiesConfig.hardcoreHearts;
    }
}
