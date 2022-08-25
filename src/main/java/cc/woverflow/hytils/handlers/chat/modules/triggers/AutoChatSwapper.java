/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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

package cc.woverflow.hytils.handlers.chat.modules.triggers;

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.Notifications;
import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import cc.woverflow.chatting.config.ChattingConfig;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import cc.woverflow.hytils.handlers.language.LanguageData;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoChatSwapper implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 3;
    }

    @SuppressWarnings("all")
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        Multithreading.runAsync(() -> {
            final Matcher statusMatcherLeave = getLanguage().autoChatSwapperPartyStatusRegex.matcher(UTextComponent.Companion.stripFormatting(event.message.getUnformattedText())); // leaving party
            final Matcher statusMatcherJoin = getLanguage().autoChatSwapperPartyStatusRegex2.matcher(UTextComponent.Companion.stripFormatting(event.message.getUnformattedText())); // joining party
            if (statusMatcherLeave.matches()) {
                MinecraftForge.EVENT_BUS.register(new ChatChannelMessagePreventer());
                switch (HytilsConfig.chatSwapperReturnChannel) {
                    case 0:
                    default:
                        HytilsReborn.INSTANCE.getCommandQueue().queue("/chat a");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> (StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/ac") || ((ChatTab) tab).getPrefix().isEmpty())).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", () -> {
                                    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                });
                            }
                        }
                        break;
                    case 1:
                        HytilsReborn.INSTANCE.getCommandQueue().queue("/chat g");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/gc")).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", () -> {
                                    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                });
                            }
                        }
                        break;
                    case 2:
                        HytilsReborn.INSTANCE.getCommandQueue().queue("/chat o");
                        if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                            ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                            Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/oc")).findFirst();
                            if (optional.isPresent()) {
                                ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                                Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", () -> {
                                    ChatTabs.INSTANCE.setCurrentTab(currentTab);
                                });
                            }
                        }
                        break;
                }
            } else if (statusMatcherJoin.matches() && HytilsConfig.chatSwapper) {
                MinecraftForge.EVENT_BUS.register(new ChatChannelMessagePreventer());
                HytilsReborn.INSTANCE.getCommandQueue().queue("/chat p");
                if (HytilsReborn.INSTANCE.isChatting && ChattingConfig.INSTANCE.getChatTabs() && HytilsConfig.chattingIntegration) {
                    ChatTab currentTab = ChatTabs.INSTANCE.getCurrentTab();
                    Optional optional = ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> StringUtils.startsWithIgnoreCase(((ChatTab) tab).getPrefix(), "/pc")).findFirst();
                    if (optional.isPresent()) {
                        ChatTabs.INSTANCE.setCurrentTab(((ChatTab) optional.get()));
                        Notifications.INSTANCE.send("Hytils Reborn", "Hytils Reborn has automatically switched to the " + ChatTabs.INSTANCE.getCurrentTab().getName() + " chat tab. Click to revert.", () -> {
                            ChatTabs.INSTANCE.setCurrentTab(currentTab);
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.chatSwapper;
    }

    public static class ChatChannelMessagePreventer {

        private boolean hasDetected;
        private final ScheduledFuture<?> unregisterTimer;

        ChatChannelMessagePreventer() { // if the message somehow doesn't send, we unregister after 20 seconds
            this.unregisterTimer = Multithreading.submitScheduled(() -> { // to prevent blocking the next time it's used
                if (!this.hasDetected) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }, 20, TimeUnit.SECONDS);
        }


        @SubscribeEvent
        public void checkForAlreadyInThisChannelThing(ClientChatReceivedEvent event) {
            final LanguageData language = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent();
            final String message = event.message.getUnformattedText();
            if (language.autoChatSwapperAlreadyInChannel.equals(message)
                || (HytilsConfig.hideAllChatMessage && language.autoChatSwapperChannelSwapRegex.matcher(message).matches())) {
                this.unregisterTimer.cancel(false);
                this.hasDetected = true;
                event.setCanceled(true);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}
