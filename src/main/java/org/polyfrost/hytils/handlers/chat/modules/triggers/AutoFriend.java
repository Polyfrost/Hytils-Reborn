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

import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.chat.ChatReceiveModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;

import java.util.regex.Matcher;

/**
 * Taken and adapted from AutoFriend under MIT
 * https://github.com/minemanpi/AutoFriend/blob/master/LICENSE
 *
 * @author 2Pi
 */
public class AutoFriend implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replace("\n", "");
        Matcher matcher = getLanguage().autoFriendPatternRegex.matcher(message);
        if (message.contains(": ")) return;
        if (matcher.find()) {
            String name = matcher.group("name");
            if (name.startsWith("[")) {
                name = name.substring(name.indexOf("] ") + 2);
            }
            event.setCanceled(true);
            HytilsReborn.INSTANCE.getCommandQueue().queue("/friend " + name);
            Notifications.INSTANCE.enqueue(Notifications.Type.Info, HytilsReborn.MOD_NAME, "Automatically added " + name + " to your friend list.");
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.autoFriend;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
