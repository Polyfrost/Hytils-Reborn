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

package club.sk1er.hytilities.handlers.chat;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.chat.adblock.AdBlocker;
import club.sk1er.hytilities.handlers.chat.cleaner.ChatCleaner;
import club.sk1er.hytilities.handlers.chat.connected.ConnectedMessage;
import club.sk1er.hytilities.handlers.chat.events.AchievementEvent;
import club.sk1er.hytilities.handlers.chat.events.LevelupEvent;
import club.sk1er.hytilities.handlers.chat.guild.GuildWelcomer;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import club.sk1er.hytilities.handlers.chat.swapper.AutoChatSwapper;
import club.sk1er.hytilities.handlers.chat.watchdog.ThankWatchdog;
import club.sk1er.hytilities.handlers.chat.whitechat.WhiteChat;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

    private final List<ChatModule> moduleList = new ArrayList<>();

    public ChatHandler() {
        this.registerModule(new AdBlocker());
        this.registerModule(new ChatCleaner());
        this.registerModule(new ChatRestyler());
        this.registerModule(new WhiteChat());
        this.registerModule(new LevelupEvent());
        this.registerModule(new AchievementEvent());
        this.registerModule(new AutoChatSwapper());
        this.registerModule(new ConnectedMessage());
        this.registerModule(new ThankWatchdog());
        this.registerModule(new GuildWelcomer());

        // reinitializing these seems to break them
        this.registerModule(Hytilities.INSTANCE.getAutoQueue());
        this.registerModule(Hytilities.INSTANCE.getLocrawUtil());
    }

    private void registerModule(ChatModule chatModule) {
        this.moduleList.add(chatModule);
    }

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel()) {
            return;
        }

        for (ChatModule module : this.moduleList) {
            if (module.isEnabled()) {
                module.onChatEvent(event);
            }
        }
    }
}
