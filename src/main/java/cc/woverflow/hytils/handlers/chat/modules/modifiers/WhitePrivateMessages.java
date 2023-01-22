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

package cc.woverflow.hytils.handlers.chat.modules.modifiers;

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class WhitePrivateMessages implements ChatReceiveModule {

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final Matcher matcher = getLanguage().privateMessageWhiteChatRegex.matcher(event.message.getFormattedText());
        if (matcher.find(0)) {
            boolean foundStart = false;
            for (IChatComponent sibling : event.message.getSiblings()) {
                if (sibling.getFormattedText().equals("§7: §r")) foundStart = true;
                if (foundStart && sibling.getChatStyle().getColor() == EnumChatFormatting.GRAY) {
                    sibling.getChatStyle().setColor(EnumChatFormatting.WHITE);
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.whitePrivateMessages;
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
