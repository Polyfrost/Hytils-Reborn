/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.handlers.game.housing;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HousingMusic {

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent event) {
        if (HypixelUtils.INSTANCE.isHypixel() && HytilsConfig.muteHousingMusic) {
            LocrawInfo locraw = HypixelUtils.INSTANCE.getLocrawInfo();
            if (locraw != null && locraw.getGameType() == LocrawInfo.GameType.HOUSING && event.name.startsWith("note.")) {
                event.result = null;
            }
        }
    }
}
