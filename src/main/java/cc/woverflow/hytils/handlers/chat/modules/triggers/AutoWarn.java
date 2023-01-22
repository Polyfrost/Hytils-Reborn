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

package cc.woverflow.hytils.handlers.chat.modules.triggers;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class AutoWarn implements ChatReceiveModule {
    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.notifyWhenKick;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (UTextComponent.Companion.stripFormatting(event.message.getUnformattedText()).startsWith("A kick")) {
            UChat.say(HytilsConfig.putInCaps ? "/pc ---------REQUEUE, I'VE BEEN KICKED!---------" : "/pc ---------I've been kicked, please requeue!---------");
        }
    }
}
