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

package org.polyfrost.hytils.handlers.lobby.limbo;

import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.oneconfig.api.hypixel.v0.HypixelAPI;

public class LimboPmDing {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        HypixelAPI.Location location = HypixelAPI.getLocation();
        if (HypixelUtils.INSTANCE.isHypixel() && location.getServerId().equals("limbo") && event.message.getFormattedText().startsWith("§dFrom §r") && HytilsConfig.limboDing) {
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1f, 1f);
        }
    }
}
