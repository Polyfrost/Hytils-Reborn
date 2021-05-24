/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.handlers.game.housing;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import gg.essential.api.EssentialAPI;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HousingMusic {

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent event) {
        if (EssentialAPI.getMinecraftUtil().isHypixel() && HytilitiesConfig.muteHousingMusic) {
            LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();
            if (locraw != null && locraw.getGameType() == GameType.HOUSING && event.name.startsWith("note.")) {
                event.result = null;
            }
        }
    }
}
