/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
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

package club.sk1er.hytilities.handlers.lobby.limbo;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.modules.modifiers.DefaultChatRestyler;
import club.sk1er.hytilities.handlers.language.LanguageData;
import club.sk1er.hytilities.tweaker.asm.MinecraftTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.tree.ClassNode;

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

        // While in Limbo, if the player uses a /play command or is party warped,
        // the AFK title text still remains, regardless of whether the player is moving.
        GuiIngame guiIngame = Minecraft.getMinecraft().ingameGUI;
        if (guiIngame.displayedTitle.equals("\u00a7cYou are AFK\u00a7r") && guiIngame.displayedSubTitle.equals("\u00a7eMove around to return to the lobby.\u00a7r")) {
            guiIngame.displayTitle(null, null, -1, -1, -1);
        }
    }

    /** Used in {@link MinecraftTransformer#transform(ClassNode, String)} */
    @SuppressWarnings("unused")
    public static boolean shouldLimitFramerate() {
        return (!Display.isActive() || limboStatus) && HytilitiesConfig.limboLimiter && time * 20 >= 5
            && Minecraft.getMinecraft().gameSettings.limitFramerate > 15;
        // if the FPS limit is > 15, don't activate, as you would be increasing the fps limit
    }
}
