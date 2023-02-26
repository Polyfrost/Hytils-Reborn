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

package cc.woverflow.hytils.command;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Description;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.woverflow.hytils.HytilsReborn;
import com.mojang.authlib.GameProfile;

@Command("block")
public class BlockCommand {

    @Main
    public void handle() {
        UChat.chat(ChatColor.RED + "Usage: /block <player>");
    }

    @Main(description = "Adds a player to the ignore list.")
    private void main(@Description("Player Name") GameProfile player) {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            String name = player.getName();
            HytilsReborn.INSTANCE.getCommandQueue().queue("/ignore add " + name);
        } else {
            UChat.chat(ChatColor.RED + "You must be on Hypixel to use this command.");
        }
    }
}
