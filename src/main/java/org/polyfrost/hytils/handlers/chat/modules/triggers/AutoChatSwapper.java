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

package org.polyfrost.hytils.handlers.chat.modules.triggers;

import dev.deftu.omnicore.api.client.chat.OmniClientChatSender;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.chatting.chat.ChatTab;
import org.polyfrost.chatting.chat.ChatTabs;
import org.polyfrost.chatting.config.ChattingConfig;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.hytils.handlers.language.LanguageData;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoChatSwapper implements ChatReceiveModule {
    @SuppressWarnings("all")
    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        Multithreading.submit(() -> {
            final Matcher statusMatcherLeave = getLanguage().autoChatSwapperPartyStatusRegex.matcher(event.getFullyUnformattedMessage()); // leaving party
            final Matcher statusMatcherJoin = getLanguage().autoChatSwapperPartyStatusRegex2.matcher(event.getFullyUnformattedMessage()); // joining party
            if (statusMatcherLeave.matches()) {
                EventManager.INSTANCE.register(new ChatChannelMessagePreventer());
                switch (HytilsConfig.chatSwapperReturnChannel) {
                    case 0:
                    default:
                        OmniClientChatSender.queue("/chat a");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> (StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/ac") || ((ChatTab) tab).getPrefix().isEmpty())).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                //Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", null, Units.seconds(4), null, () -> {
                                //    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                //});
                                //todo
                            }
                        }
                        break;
                    case 1:
                        OmniClientChatSender.queue("/chat g");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/gc")).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                //Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", null, Units.seconds(4), null, () -> {
                                //    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                //});
                                //todo
                            }
                        }
                        break;
                    case 2:
                        OmniClientChatSender.queue("/chat o");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/oc")).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                //Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", null, Units.seconds(4), null, () -> {
                                //    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                //});
                                //todo
                            }
                        }
                        break;
                }
            } else if (statusMatcherJoin.matches() && HytilsConfig.chatSwapper) {
                EventManager.INSTANCE.register(new ChatChannelMessagePreventer());
                OmniClientChatSender.queue("/chat p");
                if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                    ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                    Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/pc")).findFirst();
                    if (optional.isPresent()) {
                        ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                        //Notifications.INSTANCE.enqueue(Notifications.Type.Info, "Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", null, Units.seconds(4), null, () -> {
                        //    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                        //});
                        //todo
                    }
                }
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.chatSwapper;
    }

    @Override
    public int getPriority() {
        return 3;
    }

    public static class ChatChannelMessagePreventer {

        private boolean hasDetected;
        private final ScheduledFuture<?> unregisterTimer;

        ChatChannelMessagePreventer() { // if the message somehow doesn't send, we unregister after 20 seconds
            this.unregisterTimer = Multithreading.submitScheduled(() -> { // to prevent blocking the next time it's used
                if (!this.hasDetected) {
                    EventManager.INSTANCE.unregister(this);
                }
            }, 20, TimeUnit.SECONDS);
        }


        @Subscribe
        public void checkForAlreadyInThisChannelThing(ChatEvent.Receive event) {
            final LanguageData language = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
            final String message = event.getFullyUnformattedMessage();
            if (language.autoChatSwapperAlreadyInChannel.equals(message)
                || (HytilsConfig.hideAllChatMessage && language.autoChatSwapperChannelSwapRegex.matcher(message).matches())) {
                this.unregisterTimer.cancel(false);
                this.hasDetected = true;
                event.cancelled = true;
                EventManager.INSTANCE.unregister(this);
            }
        }
    }
}
