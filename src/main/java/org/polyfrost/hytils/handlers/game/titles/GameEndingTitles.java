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

import net.minecraft.util.EnumChatFormatting;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.events.TitleEvent;

public class GameEndingTitles {
    @Subscribe
    public void onTitle(TitleEvent event) {
        if (!HypixelUtils.isHypixel() || !HytilsConfig.hideGameEndingTitles) {
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
            case "GAME OVER":
                event.cancelled = true;
                break;
        }
    }
}
