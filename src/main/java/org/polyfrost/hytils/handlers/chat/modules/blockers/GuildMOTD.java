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

package org.polyfrost.hytils.handlers.chat.modules.blockers;

import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.oneconfig.api.event.v1.events.ChatReceiveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class GuildMOTD implements ChatReceiveModule {

    /**
     * True if the player just joined the server very recently.
     * MOTD can only be received when logging into Hypixel.
     */
    private boolean canCheckMOTD;
    /**
     * True if the received chat messages are considered part of the guild MOTD.
     */
    private boolean isMOTD;

    public GuildMOTD() {
        // Necessary to check for ClientConnectedToServerEvent.
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) { // TODO
        // Allow checking of MOTD immediately after joining Hypixel.
        canCheckMOTD = true;
        isMOTD = false;
        // Stop checking, as it shouldn't be possible for further messages to be the MOTD.
        // May have to adjust the timing.
        Multithreading.schedule(() -> canCheckMOTD = false, 3L, TimeUnit.SECONDS);
    }

    @Override
    public void onMessageReceived(@NotNull ChatReceiveEvent event) {
        if (canCheckMOTD) {
            // MOTD line breaker is already trimmed to chat width.
            if (event.getFullyUnformattedMessage().startsWith("------")) {
                // Received the first or last line of MOTD.
                isMOTD = !isMOTD;
                // Hide the MOTD line breaker.
                event.cancelled = true;
            } else if (isMOTD) {
                // After reading the first line, cancel any subsequent chat lines until the last line is read.
                event.cancelled = true;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.guildMotd;
    }
}
