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

import net.minecraft.util.IChatComponent;
import org.polyfrost.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import org.polyfrost.oneconfig.api.event.v1.events.ChatReceiveEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class LimboPmDing {
    @Subscribe
    public void onChat(ChatReceiveEvent event) {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        IChatComponent message = event.getMessage();
        if (HypixelUtils.isHypixel() && "limbo".equals(location.getServerName().orElse(null)) && message.getFormattedText().startsWith("§dFrom §r") && HytilsConfig.limboDing) {
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1f, 1f);
        }
    }
}
