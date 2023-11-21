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

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.DefaultChatRestyler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class LimboLimiter {

    private static boolean limboStatus;
    private static long time;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (limboStatus) {
            ++time;
        } else {
            time = 0;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        limboStatus = false;
        DefaultChatRestyler.reset(); // putting this here so we don't have to make a new event class just to do this
    }

    public static boolean shouldLimitFramerate() {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HypixelUtils.INSTANCE.isHypixel() && locraw != null && locraw.getServerId().equals("limbo")) limboStatus = true;
        return (!Display.isActive() || limboStatus) && HytilsConfig.limboLimiter && time * 20 >= 5
            && Minecraft.getMinecraft().gameSettings.limitFramerate > 15;
        // if the FPS limit is > 15, don't activate, as you would be increasing the fps limit
    }

    public static boolean inLimbo() {
        return limboStatus;
    }

}
