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

package club.sk1er.hytilities.handlers.silent;

import club.sk1er.hytilities.Hytilities;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class SilentRemoval {

    private final List<String> silentUsers = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChat(ClientChatReceivedEvent event) {
        Matcher matcher = Hytilities.INSTANCE.getLanguageHandler().getCurrent().silentRemovalLeaveMessageRegex.matcher(event.message.getUnformattedText());

        if (matcher.matches()) {
            // not a friend anymore :(
            for (String friend : silentUsers) {
                if (matcher.group("player").equalsIgnoreCase(friend)) {
                    Hytilities.INSTANCE.getCommandQueue().queue("/f remove " + friend);
                    silentUsers.remove(friend);
                }
            }
        }
    }

    public List<String> getSilentUsers() {
        return silentUsers;
    }
}
