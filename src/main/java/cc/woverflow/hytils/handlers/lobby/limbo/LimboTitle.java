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

package cc.woverflow.hytils.handlers.lobby.limbo;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.events.TitleEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LimboTitle {
    // Prevent the Limbo AFK text from being stuck on the player's screen indefinitely.
    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        // The player has the AFK title text shown.
        if (event.getTitle().equals("§cYou are AFK§r") && event.getSubtitle().equals("§eMove around to return to the lobby.§r")) {
            // The player moved. Also check if the user has the Remove Limbo AFK Title feature on.
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0 || HytilsConfig.hideLimboTitle) {
                // Forcefully remove the title text.
                event.setCanceled(true);
            }
        }
    }
}
