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
import org.polyfrost.oneconfig.api.event.v1.events.SoundPlayedEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class SilentLobby {
    @Subscribe
    public void onSoundPlay(SoundPlayedEvent event) { // TODO
        if (HypixelUtils.isHypixel() && !HypixelUtils.getLocation().inGame()) {
            String sound = event.getName();
            if (HytilsConfig.silentLobby && !sound.startsWith("gui.")) {
                event.setSound(null);
            } else {
                /*
                This code is taken from LobbySounds by Sk1er LLC under the GPL License:
                https://github.com/Sk1erLLC/LobbySounds/blob/master/LICENSE
                Only changes to adapt to this project have been made, as well as additions.
                 */
                if (sound.startsWith("step.") && HytilsConfig.lobbyDisableSteppingSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("mob.slime") && HytilsConfig.lobbyDisableSlimeSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("mob.enderdragon") && HytilsConfig.lobbyDisableDragonSounds) {
                    event.setSound(null);
                }

                if ((sound.startsWith("mob.wither") || sound.startsWith("mob.skeleton")) && HytilsConfig.lobbyDisableWitherSounds) {
                    event.setSound(null);
                }

                if (sound.equals("random.orb") && HytilsConfig.lobbyDisableExperienceOrbSounds) {
                    event.setSound(null);
                }

                if (sound.equals("random.pop") && HytilsConfig.lobbyDisableItemPickupSounds) {
                    event.setSound(null);
                }

                if (sound.equals("game.tnt.primed") && HytilsConfig.lobbyDisablePrimedTntSounds) {
                    event.setSound(null);
                }

                if (sound.equals("random.explode") && HytilsConfig.lobbyDisableExplosionSounds) {
                    event.setSound(null);
                }

                if (sound.equals("mob.chicken.plop") && HytilsConfig.lobbyDisableDeliveryManSounds) {
                    event.setSound(null);
                }

                if ((sound.startsWith("note.") || sound.equals("random.click")) && HytilsConfig.lobbyDisableNoteBlockSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("fireworks") && HytilsConfig.lobbyDisableFireworkSounds) {
                    event.setSound(null);
                }

                if (sound.equals("random.levelup") && HytilsConfig.lobbyDisableLevelupSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("mob.bat") && HytilsConfig.lobbyDisableBatSounds) {
                    event.setSound(null);
                }

                if (sound.equals("fire.fire") && HytilsConfig.lobbyDisableFireSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("mob.endermen") && HytilsConfig.lobbyDisableEndermanSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("random.bow") && HytilsConfig.lobbyDisableArrowSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("random.door") && HytilsConfig.lobbyDisableDoorSounds) {
                    event.setSound(null);
                }

                if (sound.startsWith("portal.portal") && HytilsConfig.lobbyDisablePortalSounds) {
                    event.setSound(null);
                }
            }
        }
    }
}
