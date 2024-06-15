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

import org.polyfrost.oneconfig.utils.v1.Notifications;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class SilentRemoval implements ChatReceiveModule {

    private static final Set<String> silentUsers = new HashSet<>();

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final Matcher matcher = getLanguage().silentRemovalLeaveMessageRegex.matcher(EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText()));

        if (matcher.matches()) {
            // not a friend anymore :(
            for (String friend : silentUsers) {
                if (matcher.group("player").equalsIgnoreCase(friend)) {
                    HytilsReborn.INSTANCE.getCommandQueue().queue("/f remove " + friend);
                    silentUsers.remove(friend);
                    Notifications.INSTANCE.send("Hytils Reborn", "Silently removed " + friend + " from your friends list.");
                }
            }
        }
    }

    public static Set<String> getSilentUsers() {
        return silentUsers;
    }

    @Override
    public int getPriority() {
        return -10;
    }
}
