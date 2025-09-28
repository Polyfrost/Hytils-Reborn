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

package org.polyfrost.hytils.handlers.chat;

import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.handlers.chat.modules.blockers.*;
import org.polyfrost.hytils.handlers.chat.modules.events.AchievementEvent;
import org.polyfrost.hytils.handlers.chat.modules.events.LevelupEvent;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.DefaultChatRestyler;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.GameStartCompactor;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.WhiteChat;
import org.polyfrost.hytils.handlers.chat.modules.modifiers.WhitePrivateMessages;
import org.polyfrost.hytils.handlers.chat.modules.triggers.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatHandler {
    private final List<ChatReceiveModule> receiveModules = new ArrayList<>();
    private final List<ChatSendModule> sendModules = new ArrayList<>();
    private final List<ChatReceiveResetModule> resetModules = new ArrayList<>();

    public ChatHandler() {

        // Blockers

        this.registerModule(new AdBlocker());
        this.registerModule(new AntiGG());
        this.registerModule(new AntiGL());
        this.registerModule(new AutoWB());
        this.registerModule(new BedwarsAdvertisementsRemover());
        this.registerModule(new BridgeOwnGoalDeathRemover());
        this.registerModule(new ConnectedMessage());
        this.registerModule(new ConnectionStatusRemover());
        this.registerModule(new CurseOfSpamRemover());
        this.registerModule(new DiscordSafetyWarningRemover());
        this.registerModule(new DuelsBlockTrail());
        this.registerModule(new DuelsNoStatsChange());
        this.registerModule(new EarnedCoinsAndExpRemover());
        this.registerModule(new GameAnnouncementsRemover());
        this.registerModule(new GameTipsRemover());
        this.registerModule(new GiftBlocker());
        this.registerModule(new GuildMOTD());
        this.registerModule(new HotPotatoRemover());
        this.registerModule(new HypeLimitReminderRemover());
        this.registerModule(new KarmaRemover());
        this.registerModule(new LobbyFishingAnnouncementRemover());
        this.registerModule(new LobbyStatusRemover());
        this.registerModule(new MvpEmotesRemover());
        this.registerModule(new NonCooldownBlocker());
        this.registerModule(new OnlineStatusRemover());
        this.registerModule(new QuestBlocker());
        this.registerModule(new ReplayRecordedRemover());
        this.registerModule(new SeasonalCollectedRemover());
        this.registerDualModule(new ShoutBlocker());
        this.registerModule(new SkyblockWelcomeRemover());
        this.registerModule(new SoulWellAnnouncerRemover());
        this.registerModule(new StatsMessageRemover());
        this.registerModule(new TicketMachineRemover());
        this.registerModule(new TipMessageRemover());

        // Events

        this.registerModule(new AchievementEvent());
        this.registerModule(new LevelupEvent());

        // Modifiers

        this.registerModule(new DefaultChatRestyler());
        this.registerModule(new GameStartCompactor());
        this.registerModule(new WhiteChat());
        this.registerModule(new WhitePrivateMessages());

        // Triggers
        this.registerModule(new AutoAfkReply());
        this.registerModule(new AutoChatSwapper());
        this.registerModule(new AutoFriend());
        this.registerModule(new AutoGG());
        this.registerModule(new AutoGL());
        this.registerModule(new AutoPartyWarpConfirm());
        this.registerModule(new AutoVictory());
        this.registerModule(new AutoWarn());
        this.registerModule(new GuildWelcomer());
        this.registerModule(new SilentRemoval());
        this.registerModule(new ThankWatchdog());

        // Priority

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

    @Subscribe
    public void handleWorldLeave(WorldEvent.Unload e) {
        for (ChatReceiveResetModule module : this.resetModules) {
            module.reset();
        }
    }

    @Subscribe
    public void handleChat(ChatEvent.Receive event) {
        if (!HypixelUtils.isHypixel()) {
            return;
        }

        // These don't cast to ChatReceiveModule for god knows why, so we can't include them in receiveModules.
        // Therefore, we manually trigger them here.
        HytilsReborn.INSTANCE.getAutoQueue().onMessageReceived(event);

        for (ChatReceiveModule module : this.receiveModules) {
            try {
                if (module.isEnabled()) {
                    module.onMessageReceived(event);
                    if (event.cancelled) {
                        return;
                    }
                }
            } catch (Exception e) {
                HytilsReborn.INSTANCE.getLogger().error("An error occurred while handling a chat message with module " + module.getClass().getSimpleName(), e);
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
        if (!HypixelUtils.isHypixel()) {
            return message;
        }

        for (ChatSendModule module : this.sendModules) {
            try {
                if (module.isEnabled()) {
                    message = module.onMessageSend(message);
                    if (message == null) {
                        return null;
                    }
                }
            } catch (Exception e) {
                HytilsReborn.INSTANCE.getLogger().error("An error occurred while handling a sent message with module " + module.getClass().getSimpleName(), e);
            }
        }

        return message;
    }
}
