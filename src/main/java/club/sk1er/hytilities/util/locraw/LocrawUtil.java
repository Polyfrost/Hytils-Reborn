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

package club.sk1er.hytilities.util.locraw;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.mods.core.util.MinecraftUtils;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class LocrawUtil implements ChatReceiveModule {

    private final Gson gson = new Gson();
    private LocrawInformation locrawInformation;
    private boolean listening;
    private int tick;
    private boolean playerSentCommand = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !MinecraftUtils.isHypixel() || this.tick >= 20) {
            return;
        }

        this.tick++;
        if (this.tick == 20) {
            this.listening = true;
            Hytilities.INSTANCE.getCommandQueue().queue("/locraw");
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        tick = 0;
    }

    public String onMessageSend(@NotNull String message) {
        if (message.startsWith("/locraw") && !this.listening) {
            this.playerSentCommand = true;
        }
        return message;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        try {
            // Had some false positives while testing, so this is here just to be safe.
            String msg = event.message.getUnformattedTextForChat();
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
                    this.playerSentCommand = false;
                    this.listening = false;
                }
            }
        } catch (Exception ignored) {
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
