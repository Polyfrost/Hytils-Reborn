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
import org.polyfrost.hytils.handlers.chat.modules.modifiers.DefaultChatRestyler;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

public class LimboLimiter {

    private static boolean limboStatus;
    private static long time;

    @Subscribe
    public void onTick(TickEvent event) {
        if (limboStatus) {
            ++time;
        } else {
            time = 0;
        }
    }

    @Subscribe
    public void onWorldChange(WorldEvent.Unload event) { // TODO
        limboStatus = false;
        DefaultChatRestyler.reset(); // putting this here so we don't have to make a new event class just to do this
    }

    public static boolean shouldLimitFramerate() {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (HypixelUtils.isHypixel() && "limbo".equals(location.getServerName().orElse(null))) limboStatus = true;
        return (!Display.isActive() || limboStatus) && HytilsConfig.limboLimiter && time * 20 >= 5
            && Minecraft.getMinecraft().gameSettings.limitFramerate > 15;
        // if the FPS limit is > 15, don't activate, as you would be increasing the fps limit
    }

    public static boolean inLimbo() {
        return limboStatus;
    }

}
