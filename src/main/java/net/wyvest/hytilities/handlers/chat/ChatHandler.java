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

package net.wyvest.hytilities.handlers.chat;

import gg.essential.api.EssentialAPI;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wyvest.hytilities.Hytilities;
import net.wyvest.hytilities.handlers.chat.modules.blockers.*;
import net.wyvest.hytilities.handlers.chat.modules.events.AchievementEvent;
import net.wyvest.hytilities.handlers.chat.modules.events.LevelupEvent;
import net.wyvest.hytilities.handlers.chat.modules.modifiers.*;
import net.wyvest.hytilities.handlers.chat.modules.triggers.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatHandler {

    private final List<ChatReceiveModule> receiveModules = new ArrayList<>();
    private final List<ChatSendModule> sendModules = new ArrayList<>();
    private final List<ChatReceiveResetModule> resetModules = new ArrayList<>();

    public ChatHandler() {
        this.registerModule(new AdBlocker());
        this.registerModule(new GuildMOTD());
        this.registerModule(new WhiteChat());
        this.registerModule(new WhitePrivateMessages());
        this.registerModule(new LevelupEvent());
        this.registerModule(new GuildWelcomer());
        this.registerModule(new ThankWatchdog());
        this.registerModule(new AutoChatReportConfirm());
        this.registerModule(new AutoChatSwapper());
        this.registerModule(new AchievementEvent());
        this.registerModule(new ConnectedMessage());
        this.registerModule(new GameStartCompactor());
        this.registerModule(new DefaultChatRestyler());
        this.registerModule(new LobbyStatusRemover());
        this.registerModule(new MvpEmotesRemover());
        this.registerModule(new MysteryBoxRemover());
        this.registerModule(new GameAnnouncementsRemover());
        this.registerModule(new HypeLimitReminderRemover());
        this.registerModule(new SoulWellAnnouncerRemover());
        this.registerModule(new BedwarsAdvertisementsRemover());
        this.registerModule(new AutoAPI());
        this.registerModule(new AntiGL());
        this.registerModule(new AutoGL());
        this.registerModule(new ConnectionStatusRemover());
        this.registerModule(new CurseOfSpamRemover());
        this.registerModule(new SkyblockWelcomeRemover());
        this.registerModule(new QuestBlocker());
        this.registerModule(new GiftBlocker());
        this.registerModule(new GrinchPresentsRemover());
        this.registerModule(new BoosterRemover());
        this.registerModule(new AutoVictory());

        this.registerDualModule(new ShoutBlocker());

        this.sendModules.sort(Comparator.comparingInt(ChatModule::getPriority));
    }

    private void registerModule(ChatReceiveResetModule chatModule) {
        receiveModules.add(chatModule);
        resetModules.add(chatModule);
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
    public void handleWorldLeave(WorldEvent.Unload e) {
        for (ChatReceiveResetModule module : this.resetModules) {
            module.reset();
        }
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
