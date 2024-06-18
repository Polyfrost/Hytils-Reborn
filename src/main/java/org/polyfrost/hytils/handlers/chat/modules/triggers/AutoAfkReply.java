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

import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;

import java.util.regex.Matcher;

public class AutoAfkReply implements ChatReceiveModule {
    // TODO: maybe write a general afk checker for skyblock afkers, as they won't be in limbo
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (getLocraw() != null && !getLocraw().getServerId().equals("limbo")) return;
        String message = event.message.getUnformattedText();
        Matcher matcher = getLanguage().autoAfkReplyPatternRegex.matcher(message);
        if (matcher.matches()) {
            UChat.say("/msg " + matcher.group(2) + " Hey "  + matcher.group(2) + ", I am currently AFK!");
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoReplyAfk;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
