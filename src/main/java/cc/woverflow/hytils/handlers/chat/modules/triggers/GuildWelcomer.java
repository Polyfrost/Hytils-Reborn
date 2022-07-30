/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

import cc.woverflow.hytils.config.HytilsConfig;
import cc.woverflow.hytils.handlers.chat.ChatReceiveModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class GuildWelcomer implements ChatReceiveModule {

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        final String text = event.message.getUnformattedText();
        final Matcher matcher = getLanguage().guildPlayerJoinRegex.matcher(text);
        if (matcher.matches()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gc Welcome to the guild " + matcher.group("player") + "!");
        }
    }

    @Override
    public boolean isEnabled() {
        return HytilsConfig.guildWelcomeMessage;
    }
}
