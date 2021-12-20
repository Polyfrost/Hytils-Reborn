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

package net.wyvest.hytilities.handlers.chat.modules.blockers;

import net.wyvest.hytilities.config.HytilitiesConfig;
import net.wyvest.hytilities.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class LobbyStatusRemover implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (message.contains(":")) return;
        for (Pattern messages : getLanguage().chatCleanerJoinMessageTypes) {
            if (messages.matcher(message).find()) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilitiesConfig.lobbyStatus;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
