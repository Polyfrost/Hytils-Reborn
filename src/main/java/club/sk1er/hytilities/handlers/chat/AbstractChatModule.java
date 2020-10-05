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

package club.sk1er.hytilities.handlers.chat;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.language.LanguageData;
import club.sk1er.mods.core.universal.ChatColor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * Used to have no conflicts for classes implementing both {@link ChatSendModule} and {@link ChatReceiveModule}
 */
public interface AbstractChatModule {
    default IChatComponent colorMessage(String message) {
        return new ChatComponentText(ChatColor.translateAlternateColorCodes('&', message));
    }

    default LanguageData getLanguage() {
        return Hytilities.INSTANCE.getLanguageHandler().getCurrent();
    }
}
