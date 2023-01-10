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

package cc.woverflow.hytils.handlers.chat.modules.modifiers;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class ColoredFriendStatuses implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String message = event.message.getFormattedText().trim();

        if (HytilsConfig.coloredStatuses) {
            Matcher matcher = getLanguage().chatRestylerStatusPatternRegex.matcher(message);
            if (matcher.matches()) {
                final String status = matcher.group("status"); // TODO: fix short channel names so we can remove the 2nd group

                String channelStarter = null; // Fixes short channel name incompatibility since it randomly popped up despite what appears to be no changes
                if (matcher.group("type").contains("F")) {
                    if (HytilsConfig.shortChannelNames) channelStarter = "§aF > §r";
                    else channelStarter = "§aFriend > §r";
                }
                if (matcher.group("type").contains("G")) {
                    if (HytilsConfig.shortChannelNames) channelStarter = "§2G > §r";
                    else channelStarter = "§2Guild > §r";
                }

                if (status.equalsIgnoreCase("joined")) {
                    event.message = colorMessage(channelStarter + matcher.group("player") + " §r" + "§ajoined§e.");
                }
                if (status.equalsIgnoreCase("left")) {
                    event.message = colorMessage(channelStarter + matcher.group("player") + " §r" + "§cleft§e.");
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
