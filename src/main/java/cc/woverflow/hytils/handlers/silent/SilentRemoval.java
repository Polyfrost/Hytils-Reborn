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

package cc.woverflow.hytils.handlers.silent;

import cc.woverflow.hytils.HytilsReborn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class SilentRemoval {

    private final Set<String> silentUsers = new HashSet<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChat(ClientChatReceivedEvent event) {
        final Matcher matcher = HytilsReborn.INSTANCE.getLanguageHandler().getCurrent().silentRemovalLeaveMessageRegex.matcher(event.message.getUnformattedText());

        if (matcher.matches()) {
            // not a friend anymore :(
            for (String friend : silentUsers) {
                if (matcher.group("player").equalsIgnoreCase(friend)) {
                    HytilsReborn.INSTANCE.getCommandQueue().queue("/f remove " + friend);
                    silentUsers.remove(friend);
                }
            }
        }
    }

    public Set<String> getSilentUsers() {
        return silentUsers;
    }
}
