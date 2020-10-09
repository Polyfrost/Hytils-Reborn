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
import club.sk1er.hytilities.handlers.chat.compactor.GameStartCompactor;
import club.sk1er.hytilities.handlers.chat.connected.ConnectedMessage;
import club.sk1er.hytilities.handlers.chat.events.AchievementEvent;
import club.sk1er.hytilities.handlers.chat.events.LevelupEvent;
import club.sk1er.hytilities.handlers.chat.guild.GuildWelcomer;
import club.sk1er.hytilities.handlers.chat.restyler.ChatRestyler;
import club.sk1er.hytilities.handlers.chat.shoutblocker.ShoutBlocker;
import club.sk1er.hytilities.handlers.chat.swapper.AutoChatSwapper;
import club.sk1er.hytilities.handlers.chat.watchdog.ThankWatchdog;
import club.sk1er.hytilities.handlers.chat.whitechat.WhiteChat;
import club.sk1er.mods.core.util.MinecraftUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {
    private final List<ChatReceiveModule> receiveModules = new ArrayList<>();
    private final List<ChatSendModule> sendModules = new ArrayList<>();

    public ChatHandler() {
        this.registerReceiveModule(new AdBlocker());
        this.registerReceiveModule(new ChatCleaner());
        this.registerReceiveModule(new ChatRestyler());
        this.registerReceiveModule(new WhiteChat());
        this.registerReceiveModule(new LevelupEvent());
        this.registerReceiveModule(new AchievementEvent());
        this.registerReceiveModule(new AutoChatSwapper());
        this.registerReceiveModule(new ConnectedMessage());
        this.registerReceiveModule(new ThankWatchdog());
        this.registerReceiveModule(new GuildWelcomer());
        this.registerReceiveModule(new GameStartCompactor());
        this.registerSendAndReceiveModule(new ShoutBlocker());

        // reinitializing these seems to break them
        this.registerReceiveModule(Hytilities.INSTANCE.getAutoQueue());
        this.registerReceiveModule(Hytilities.INSTANCE.getLocrawUtil());
    }

    private void registerReceiveModule(ChatReceiveModule chatModule) {
        this.receiveModules.add(chatModule);
    }

    private void registerSendModule(ChatSendModule chatModule) {
        this.sendModules.add(chatModule);
    }

    private <T extends ChatSendModule & ChatReceiveModule> void registerSendAndReceiveModule(T chatModule) {
        this.registerReceiveModule(chatModule);
        this.registerSendModule(chatModule);
    }

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        if (!MinecraftUtils.isHypixel()) {
            return;
        }

        for (ChatReceiveModule module : this.receiveModules) {
            if (module.isReceiveModuleEnabled()) {
                module.onChatEvent(event);
            }
        }
    }

    public boolean shouldSendMessage(String message) {
        if (!MinecraftUtils.isHypixel()) {
            return true;
        }

        for (ChatSendModule module : this.sendModules) {
            if (module.isSendModuleEnabled() && !module.shouldSendMessage(message)) return false;
        }
        return true;
    }
}
