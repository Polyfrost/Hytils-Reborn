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

package cc.woverflow.hytils.handlers.chat.modules.blockers;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import gg.essential.api.utils.Multithreading;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class GuildMOTD implements ChatReceiveModule {

    /** True if the player just joined the server very recently.
     *  MOTD can only be received when logging into Hypixel. */
    private boolean canCheckMOTD;
    /** True if the received chat messages are considered part of the guild MOTD. */
    private boolean isMOTD;

    public GuildMOTD() {
        // Necessary to check for ClientConnectedToServerEvent.
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Allow checking of MOTD immediately after joining Hypixel.
        canCheckMOTD = true;
        isMOTD = false;
        // Stop checking, as it shouldn't be possible for further messages to be the MOTD.
        // May have to adjust the timing.
        Multithreading.schedule(() -> canCheckMOTD = false, 3L, TimeUnit.SECONDS);
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (canCheckMOTD) {
            // MOTD line breaker is already trimmed to chat width.
            if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedTextForChat()).startsWith("------")) {
                // Received the first or last line of MOTD.
                isMOTD = !isMOTD;
                // Hide the MOTD line breaker.
                event.setCanceled(true);
            } else if (isMOTD) {
                // After reading the first line, cancel any subsequent chat lines until the last line is read.
                event.setCanceled(true);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.guildMotd;
    }
}
