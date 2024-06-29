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

package org.polyfrost.hytils.handlers.lobby.sound;

import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;

public class SilentLobby {
    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        if (HypixelUtils.isHypixel() && !HypixelUtils.getLocation().inGame()) {
            String sound = event.name;
            if (HytilsConfig.silentLobby && !sound.startsWith("gui.")) {
                event.result = null;
            } else {
                /*
                This code is taken from LobbySounds by Sk1er LLC under the GPL License:
                https://github.com/Sk1erLLC/LobbySounds/blob/master/LICENSE
                Only changes to adapt to this project have been made, as well as additions.
                 */
                if (sound.startsWith("step.") && HytilsConfig.lobbyDisableSteppingSounds) {
                    event.result = null;
                }

                if (sound.startsWith("mob.slime") && HytilsConfig.lobbyDisableSlimeSounds) {
                    event.result = null;
                }

                if (sound.startsWith("mob.enderdragon") && HytilsConfig.lobbyDisableDragonSounds) {
                    event.result = null;
                }

                if ((sound.startsWith("mob.wither") || sound.startsWith("mob.skeleton")) && HytilsConfig.lobbyDisableWitherSounds) {
                    event.result = null;
                }

                if (sound.equals("random.orb") && HytilsConfig.lobbyDisableExperienceOrbSounds) {
                    event.result = null;
                }

                if (sound.equals("random.pop") && HytilsConfig.lobbyDisableItemPickupSounds) {
                    event.result = null;
                }

                if (sound.equals("game.tnt.primed") && HytilsConfig.lobbyDisablePrimedTntSounds) {
                    event.result = null;
                }

                if (sound.equals("random.explode") && HytilsConfig.lobbyDisableExplosionSounds) {
                    event.result = null;
                }

                if (sound.equals("mob.chicken.plop") && HytilsConfig.lobbyDisableDeliveryManSounds) {
                    event.result = null;
                }

                if ((sound.startsWith("note.") || sound.equals("random.click")) && HytilsConfig.lobbyDisableNoteBlockSounds) {
                    event.result = null;
                }

                if (sound.startsWith("fireworks") && HytilsConfig.lobbyDisableFireworkSounds) {
                    event.result = null;
                }

                if (sound.equals("random.levelup") && HytilsConfig.lobbyDisableLevelupSounds) {
                    event.result = null;
                }

                if (sound.startsWith("mob.bat") && HytilsConfig.lobbyDisableBatSounds) {
                    event.result = null;
                }

                if (sound.equals("fire.fire") && HytilsConfig.lobbyDisableFireSounds) {
                    event.result = null;
                }

                if (sound.startsWith("mob.endermen") && HytilsConfig.lobbyDisableEndermanSounds) {
                    event.result = null;
                }

                if (sound.startsWith("random.bow") && HytilsConfig.lobbyDisableArrowSounds) {
                    event.result = null;
                }

                if (sound.startsWith("random.door") && HytilsConfig.lobbyDisableDoorSounds) {
                    event.result = null;
                }

                if (sound.startsWith("portal.portal") && HytilsConfig.lobbyDisablePortalSounds) {
                    event.result = null;
                }
            }
        }
    }
}
