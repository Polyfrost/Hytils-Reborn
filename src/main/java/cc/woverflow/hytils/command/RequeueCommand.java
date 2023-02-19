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
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.handlers.cache.LocrawGamesHandler;

@Command(value = "requeue", aliases = "rq")
public class RequeueCommand {

    protected static String game = "";

    @Main
    private void requeue() {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        LocrawInfo lastLocraw = LocrawUtil.INSTANCE.getLastLocrawInfo();
        if (!HypixelUtils.INSTANCE.isHypixel() || locraw == null /*|| lastLocraw == null*/) {
            return;
        }

        if (LocrawUtil.INSTANCE.isInGame()) {
            switch (locraw.getGameType()) {
                case HOUSING:
                case SKYBLOCK:
                case REPLAY:
                    UChat.chat(ChatColor.RED + "You must be in a valid game to use this command.");
                    return;
            }
            game = locraw.getGameMode();
        } /*else if (!LocrawUtil.INSTANCE.isInGame() && !lastLocraw.getGameMode().equals("lobby")) {
            game = lastLocraw.getGameMode();
        }*/ else {
            UChat.chat(ChatColor.RED + "The last round has to be a game to use this command.");
            return;
        }

        String value = LocrawGamesHandler.locrawGames.get(locraw.getRawGameType() + "_" + game.toLowerCase());
        if (value != null) {
            game = value;
        }
        HytilsReborn.INSTANCE.getCommandQueue().queue("/play " + game.toLowerCase());
    }
}
