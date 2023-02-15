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
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;

import java.util.Objects;

@Command(value = "reque", aliases = "rq")
public class RequeCommand {
    @Main
    public void handle(@Greedy String args) {
        HytilsReborn.INSTANCE.getCommandQueue().queue("/reque " + args);
    }
    @Main
    public void reque() {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            UChat.chat(ChatColor.RED + "You must be on Hypixel to use this command.");
            return;
        }

        String game = LocrawUtil.INSTANCE.getLocrawInfo().getGameMode();
        if (game == null || LocrawUtil.INSTANCE.getLastLocrawInfo() == null) {
            UChat.chat(ChatColor.RED + "You must be in a game to use this command.");
            return;
        }
        if (game.equals("lobby") && LocrawUtil.INSTANCE.getLastLocrawInfo().getGameMode() != null) {
            game = LocrawUtil.INSTANCE.getLastLocrawInfo().getGameMode();
        }
        HytilsReborn.INSTANCE.getCommandQueue().queue("/play " + game);
    }
}
