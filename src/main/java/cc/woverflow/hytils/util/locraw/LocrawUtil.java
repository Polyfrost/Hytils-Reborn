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

package cc.woverflow.hytils.util.locraw;

import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.events.LocrawEvent;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import cc.woverflow.hytils.handlers.game.GameType;
import com.google.gson.Gson;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class LocrawUtil implements ChatReceiveModule {
    private final Gson gson = new Gson();
    private LocrawInformation locrawInformation;
    private boolean listening;
    private boolean isResending = false;
    private int tick;
    private boolean playerSentCommand = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !EssentialAPI.getMinecraftUtil().isHypixel()) {
            return;
        }

        this.tick++;
        if (this.tick == 20 || this.tick % 500 == 0) {
            this.listening = true;
            HytilsReborn.INSTANCE.getCommandQueue().queue("/locraw");
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        locrawInformation = null;
        tick = 0;
        isResending = false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public String onMessageSend(@NotNull String message) {
        if (message.startsWith("/locraw") && !this.listening) {
            if (this.tick == 22 || this.tick % 500 != 0) {
                this.playerSentCommand = true;
            }

        }
        return message;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        try {
            // Had some false positives while testing, so this is here just to be safe.
            final String msg = event.message.getUnformattedTextForChat();
            if (msg.startsWith("{")) {
                // Parse the json, and make sure that it's not null.
                this.locrawInformation = gson.fromJson(msg, LocrawInformation.class);
                if (locrawInformation != null) {
                    // Gson does not want to parse the GameType, as some stuff is different so this
                    // is just a way around that to make it properly work :)
                    this.locrawInformation.setGameType(GameType.getFromLocraw(locrawInformation.getRawGameType()));

                    // Stop listening for locraw and cancel the message.
                    if (!this.playerSentCommand) {
                        event.setCanceled(true);
                    }
                    if (!isResending) {
                        MinecraftForge.EVENT_BUS.post(new LocrawEvent(locrawInformation));
                        isResending = true;
                    }

                    this.playerSentCommand = false;
                    this.listening = false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isEnabled() {
        return listening;
    }

    public LocrawInformation getLocrawInformation() {
        return this.locrawInformation;
    }
}
