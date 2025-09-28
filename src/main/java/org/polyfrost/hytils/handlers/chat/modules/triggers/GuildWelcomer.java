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
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class GuildWelcomer implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ChatEvent.Receive event) {
        final String text = event.getFullyUnformattedMessage();
        final Matcher matcher = getLanguage().guildPlayerJoinRegex.matcher(text);
        if (matcher.matches()) {
            OmniClientChatSender.queue("/gc Welcome to the guild " + matcher.group("player") + "!");
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.guildWelcomeMessage;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
