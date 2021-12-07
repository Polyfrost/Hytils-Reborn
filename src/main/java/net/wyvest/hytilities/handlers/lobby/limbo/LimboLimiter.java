/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
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

package net.wyvest.hytilities.handlers.lobby.limbo;

import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.chat.modules.modifiers.DefaultChatRestyler;
import net.wyvest.hytilities.handlers.language.LanguageData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class LimboLimiter {

    private static boolean limboStatus;
    private static long time;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        final String message = event.message.getUnformattedText();
        final LanguageData language = Hytilities.INSTANCE.getLanguageHandler().getCurrent();

        if (message.equals(language.limboLimiterSpawned) || message.equals(language.limboLimiterAfk)) {
            limboStatus = true;
        }
    }

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

    @SuppressWarnings("unused")
    public static boolean shouldLimitFramerate() {
        return (!Display.isActive() || limboStatus) && HytilitiesConfig.limboLimiter && time * 20 >= 5
            && Minecraft.getMinecraft().gameSettings.limitFramerate > 15;
        // if the FPS limit is > 15, don't activate, as you would be increasing the fps limit
    }

    public static boolean inLimbo() {
        return limboStatus;
    }

}
