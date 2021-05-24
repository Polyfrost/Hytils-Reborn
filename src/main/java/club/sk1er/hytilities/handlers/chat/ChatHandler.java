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
import club.sk1er.hytilities.handlers.chat.modules.blockers.AdBlocker;
import club.sk1er.hytilities.handlers.chat.modules.blockers.ChatCleaner;
import club.sk1er.hytilities.handlers.chat.modules.blockers.ConnectedMessage;
import club.sk1er.hytilities.handlers.chat.modules.blockers.GiftBlocker;
import club.sk1er.hytilities.handlers.chat.modules.blockers.GuildMOTD;
import club.sk1er.hytilities.handlers.chat.modules.blockers.QuestBlocker;
import club.sk1er.hytilities.handlers.chat.modules.blockers.ShoutBlocker;
import club.sk1er.hytilities.handlers.chat.modules.events.AchievementEvent;
import club.sk1er.hytilities.handlers.chat.modules.events.LevelupEvent;
import club.sk1er.hytilities.handlers.chat.modules.modifiers.DefaultChatRestyler;
import club.sk1er.hytilities.handlers.chat.modules.modifiers.GameStartCompactor;
import club.sk1er.hytilities.handlers.chat.modules.modifiers.WhiteChat;
import club.sk1er.hytilities.handlers.chat.modules.modifiers.WhitePrivateMessages;
import club.sk1er.hytilities.handlers.chat.modules.triggers.AutoChatSwapper;
import club.sk1er.hytilities.handlers.chat.modules.triggers.GuildWelcomer;
import club.sk1er.hytilities.handlers.chat.modules.triggers.ThankWatchdog;
import club.sk1er.hytilities.asm.EntityPlayerSPTransformer;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatHandler {

    private final List<ChatReceiveModule> receiveModules = new ArrayList<>();
    private final List<ChatSendModule> sendModules = new ArrayList<>();

    public ChatHandler() {
        this.registerModule(new AdBlocker());
        this.registerModule(new GuildMOTD());
        this.registerModule(new WhiteChat());
        this.registerModule(new WhitePrivateMessages());
        this.registerModule(new ChatCleaner());
        this.registerModule(new LevelupEvent());
        this.registerModule(new GuildWelcomer());
        this.registerModule(new ThankWatchdog());
        this.registerModule(new AutoChatSwapper());
        this.registerModule(new AchievementEvent());
        this.registerModule(new ConnectedMessage());
        this.registerModule(new GameStartCompactor());
        this.registerModule(new DefaultChatRestyler());
        this.registerModule(new QuestBlocker());
        this.registerModule(new GiftBlocker());

        this.registerDualModule(new ShoutBlocker());

        this.sendModules.sort(Comparator.comparingInt(ChatModule::getPriority));
    }

    private void registerModule(ChatReceiveModule chatModule) {
        this.receiveModules.add(chatModule);
    }

    private void registerModule(ChatSendModule chatModule) {
        this.sendModules.add(chatModule);
    }

    private <T extends ChatReceiveModule & ChatSendModule> void registerDualModule(T chatModule) {
        this.registerModule((ChatReceiveModule) chatModule);
        this.registerModule((ChatSendModule) chatModule);
    }

    @SubscribeEvent
    public void handleChat(ClientChatReceivedEvent event) {
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) {
            return;
        }

        // These don't cast to ChatReceiveModule for god knows why, so we can't include them in receiveModules.
        // Therefore, we manually trigger them here.
        Hytilities.INSTANCE.getLocrawUtil().onMessageReceived(event);
        Hytilities.INSTANCE.getAutoQueue().onMessageReceived(event);

        for (ChatReceiveModule module : this.receiveModules) {
            if (module.isEnabled()) {
                module.onMessageReceived(event);
                if (event.isCanceled()) {
                    return;
                }
            }
        }
    }

    /**
     * Allow modifying sent messages, or cancelling them altogether.
     * <p>
     * Used in {@link EntityPlayerSPTransformer}.
     *
     * @param message a message that the user has sent
     * @return the modified message, or {@code null} if the message should be cancelled
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    @Nullable
    public String handleSentMessage(@NotNull String message) {
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) {
            return message;
        }

        Hytilities.INSTANCE.getLocrawUtil().onMessageSend(message);

        for (ChatSendModule module : this.sendModules) {
            if (module.isEnabled()) {
                message = module.onMessageSend(message);
                if (message == null) {
                    return null;
                }
            }
        }

        return message;
    }

    /**
     * Fixes styling when overwriting a message.
     * Store the component siblings before applying any changes to the original component.
     *
     * @param component The message being modified & restored
     * @param siblings  The message's chat component siblings
     */
    public void fixStyling(IChatComponent component, List<IChatComponent> siblings) {
        // todo: this grabs the last sibling and applies it to the whole text, instead of reapplying to the specific
        //  sibling, need to rethink this through, for now do nothing
        /*if (!siblings.isEmpty()) {
            for (IChatComponent sibling : siblings) {
                final ChatStyle chatStyle = sibling.getChatStyle();

                if (chatStyle.getChatHoverEvent() != null) {
                    component.getChatStyle().setChatHoverEvent(chatStyle.getChatHoverEvent());
                }

                if (chatStyle.getChatClickEvent() != null) {
                    component.getChatStyle().setChatClickEvent(chatStyle.getChatClickEvent());
                }
            }
        }*/
    }
}
