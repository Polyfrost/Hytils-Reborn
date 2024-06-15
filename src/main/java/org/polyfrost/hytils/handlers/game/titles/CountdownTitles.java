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

package org.polyfrost.hytils.handlers.game.titles;

import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CountdownTitles {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTitle(TitleEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel() || !HytilsConfig.hideCountdownTitles) {
            return;
        }

        switch (EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle().toUpperCase())) {
            case "60":
            case "30":
            case "10":
            case "9":
            case "8":
            case "7":
            case "6":
            case "5":
            case "4":
            case "3":
            case "2":
            case "1":
            case "10 SECONDS":
            case "❸":
            case "❷":
            case "❶":
                event.setCanceled(true);
                break;
        }
    }
}
