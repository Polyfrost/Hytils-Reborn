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

package org.polyfrost.hytils.command;

import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.textile.Text;
import dev.deftu.textile.minecraft.MCTextStyle;
import dev.deftu.textile.minecraft.TextColors;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.hytils.handlers.chat.modules.triggers.SilentRemoval;
import com.mojang.authlib.GameProfile;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Param;

import java.util.Set;
import java.util.regex.Pattern;

@Command("silentremove")
public class SilentRemoveCommand {

    protected static final Pattern usernameRegex = Pattern.compile("\\w{1,16}");

    @Handler
    public void main() {
        OmniClientChat.displayChatMessage(Text.literal("Usage: /silentremove <player>").setStyle(MCTextStyle.color(TextColors.RED)));
    }

    @Handler(description = "Adds or removes a player from the silent list.")
    private void main(@Param("Player Name") GameProfile player) {
        String name = player.getName();
        if (!usernameRegex.matcher(name).matches()) {
            OmniClientChat.displayChatMessage(Text.literal("Invalid username.").setStyle(MCTextStyle.color(TextColors.RED)));
            return;
        }
        final Set<String> silentUsers = SilentRemoval.getSilentUsers();
        if (silentUsers.contains(name)) {
            silentUsers.remove(name);
            OmniClientChat.displayChatMessage(Text.literal("Removed ").setStyle(MCTextStyle.color(TextColors.GREEN)).append(Text.literal(name).setStyle(MCTextStyle.color(TextColors.YELLOW))).append(Text.literal(" from the silent removal list.").setStyle(MCTextStyle.color(TextColors.GREEN))));
            return;
        }

        silentUsers.add(name);
        OmniClientChat.displayChatMessage("&aAdded &e" + name + " &ato the removal queue.");
    }

    @Handler(description = "Clears the silent removal queue.")
    private void clear() {
        final Set<String> silentUsers = SilentRemoval.getSilentUsers();
        if (silentUsers.isEmpty()) {
            OmniClientChat.displayChatMessage(Text.literal("Silent removal list is already empty.").setStyle(MCTextStyle.color(TextColors.RED)));
            return;
        }

        silentUsers.clear();
        OmniClientChat.displayChatMessage(Text.literal("Cleared the silent removal list.").setStyle(MCTextStyle.color(TextColors.GREEN)));
    }
}
