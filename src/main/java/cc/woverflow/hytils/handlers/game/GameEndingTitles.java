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

package cc.woverflow.hytils.handlers.game;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.TitleEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameEndingTitles {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTitle(TitleEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel() || !HytilsConfig.hideGameEndingTitles) {
            return;
        }

        switch (EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle().toUpperCase())) {
            case "YOU WIN!":
            case "YOU DIED!":
            case "YOU LOSE!":
            case "VICTORY!":
            case "GAME OVER!":
            case "RESPAWNED!":
            case "DEFEAT!":
            case "GAME END":
            case "YOU DIED":
            case "DEFEAT":
                event.setCanceled(true);
                break;
        }
    }
}
