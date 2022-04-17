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

package cc.woverflow.hytils.handlers.game;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.TitleEvent;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameEndingTitles {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTitle(TitleEvent event) {
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) return;
        if (HytilsConfig.hideGameEndingTitles) {
            switch (EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle())) {
                case "YOU WIN!":
                case "YOU DIED!":
                case "YOU LOSE!":
                case "VICTORY!":
                case "GAME OVER!":
                case "RESPAWNED!":
                case "DEFEAT!":
                case "GAME END":
                case "You Win!":
                case "You Lose!":
                    event.setCanceled(true);
                    break;
            }
        }
        if (HytilsConfig.hideGameEndingCountdownTitles) {
            switch (EnumChatFormatting.getTextWithoutFormattingCodes(event.getTitle())) {
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
                    event.setCanceled(true);
                    break;
            }
        }
    }
}
