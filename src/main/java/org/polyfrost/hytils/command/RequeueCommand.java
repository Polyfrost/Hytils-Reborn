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

import net.hypixel.data.type.GameType;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.universal.ChatColor;
import org.polyfrost.universal.UChat;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.cache.LocrawGamesHandler;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboLimiter;

@Command({"requeue", "rq"})
public class RequeueCommand {

    protected static String game = "";

    @Command(description = "Requeues you into the last game you played.")
    private void main() {
        HypixelUtils.Location location = HypixelUtils.getLocation();
        if (!HypixelUtils.isHypixel()) {
            return;
        }

        if (location.inGame()) {
            game = location.getMode().orElse(null);
            if (game == null) {
                UChat.chat(ChatColor.RED + "You must be in a valid game to use this command.");
                return;
            }
            if (location.getGameType().isPresent()) {
                switch (location.getGameType().get()) {
                    case SKYBLOCK:
                    case HOUSING:
                    case REPLAY:
                        UChat.chat(ChatColor.RED + "You must be in a valid game to use this command.");
                        return;
                }
            }
        } else if (!location.inGame() && location.wasInGame()) {
            game = location.getLastMode().orElse(null);
            if (game == null) {
                UChat.chat(ChatColor.RED + "You must be in a valid game to use this command.");
                return;
            }
            if (location.getLastGameType().isPresent()) {
                switch (location.getLastGameType().get()) {
                    case SKYBLOCK:
                        game = GameType.SKYBLOCK.getDatabaseName(); // requeues you back into SkyBlock when you are in the lobby. Useful when you get kicked for being AFK.
                        break;
                    case HOUSING:
                    case REPLAY:
                        UChat.chat(ChatColor.RED + "The last round has to be a valid game to use this command.");
                        return;
                }
            }
        } else {
            UChat.chat(ChatColor.RED + "The last round has to be a game to use this command.");
            return;
        }

        // tries to get the game name from the LocrawGamesHandler, if it doesn't exist, it will use the game name from the LocrawInfo.
        if (location.getGameType().isPresent()) {
            String value = LocrawGamesHandler.locrawGames.get(location.getGameType().get().getDatabaseName().toLowerCase() + "_" + game.toLowerCase());
            if (value != null) {
                game = value;
            }
        }

        // if the limboPlayCommandHelper is enabled and the player is in limbo, it will queue the /lobby command before the /play command.
        if (HytilsConfig.limboPlayCommandHelper && LimboLimiter.inLimbo()) {
            HytilsReborn.INSTANCE.getCommandQueue().queue("/lobby");
        }
        HytilsReborn.INSTANCE.getCommandQueue().queue("/play " + game.toLowerCase());
    }
}
