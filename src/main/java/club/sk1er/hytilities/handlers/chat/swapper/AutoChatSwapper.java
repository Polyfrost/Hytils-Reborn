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

package club.sk1er.hytilities.handlers.chat.swapper;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.chat.ChatReceiveModule;
import club.sk1er.mods.core.util.Multithreading;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class AutoChatSwapper implements ChatReceiveModule {

    @Override
    public void onChatEvent(ClientChatReceivedEvent event) {
        final Matcher statusMatcher = getLanguage().autoChatSwapperPartyStatusRegex.matcher(event.message.getUnformattedText());
        if (statusMatcher.matches()) {
            MinecraftForge.EVENT_BUS.register(new ChatChannelMessagePreventer());
            switch (HytilitiesConfig.chatSwapperReturnChannel) {
                case 0:
                default:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat a");
                    break;
                case 1:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat g");
                    break;
                case 2:
                    Hytilities.INSTANCE.getCommandQueue().queue("/chat o");
                    break;
            }
        }
    }

    @Override
    public boolean isReceiveModuleEnabled() {
        return HytilitiesConfig.chatSwapper;
    }

    public static class ChatChannelMessagePreventer {

        private boolean hasDetected;
        private final ScheduledFuture<?> unregisterTimer;

        ChatChannelMessagePreventer() { // if the message somehow doesn't send, we unregister after 20 seconds
            this.unregisterTimer = Multithreading.schedule(() -> { // to prevent blocking the next time it's used
                if (!this.hasDetected) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }, 20, TimeUnit.SECONDS);
        }


        @SubscribeEvent
        public void checkForAlreadyInThisChannelThing(ClientChatReceivedEvent event) {
            if (Hytilities.INSTANCE.getLanguageHandler().getCurrent().autoChatSwapperAlreadyInChannel.equals(event.message.getUnformattedText())
                || (HytilitiesConfig.hideAllChatMessage &&
                Hytilities.INSTANCE.getLanguageHandler().getCurrent().autoChatSwapperChannelSwapRegex.matcher(event.message.getUnformattedText()).matches())
            ) {
                unregisterTimer.cancel(false);
                hasDetected = true;
                event.setCanceled(true);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

}
