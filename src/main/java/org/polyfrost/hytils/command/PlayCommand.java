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

import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;
import org.polyfrost.universal.ChatColor;
import org.polyfrost.universal.UChat;
import org.polyfrost.oneconfig.utils.v1.Multithreading;
import org.polyfrost.oneconfig.utils.v1.NetworkUtils;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.hytils.HytilsReborn;
import org.polyfrost.hytils.command.parser.GameName;
import org.polyfrost.hytils.command.parser.GameNameParser;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.hytils.handlers.lobby.limbo.LimboLimiter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Command("play")
public class PlayCommand {
    private static Map<String, String> games = new HashMap<>();

    public static void init() {
        Multithreading.submit(() -> {
            try {
                String url = "https://data.woverflow.cc/games.json";
                String content = NetworkUtils.getString(url);
                Type stringStringMap = new TypeToken<HashMap<String, String>>() {
                }.getType();
                games = new Gson().fromJson(content, stringStringMap);
            } catch (JsonSyntaxException e) {
                HytilsReborn.INSTANCE.getLogger().error("Failed to fetch /play game list.", e);
            }

            Minecraft.getMinecraft().addScheduledTask(() -> {
                CommandManager.INSTANCE.registerParser(new GameNameParser(games));
                CommandManager.registerCommand(new PlayCommand());
            });
        });
    }
    @Command
    private void main(GameName game) {
        boolean autocompletePlayCommands = HytilsConfig.autocompletePlayCommands;
        if (!HypixelUtils.isHypixel()) {
            HytilsReborn.INSTANCE.getCommandQueue().queue("/play " + game.name);
            return;
        }

        String command = game.name;
        if (autocompletePlayCommands) {
            String value = games.get(game.name.toLowerCase(Locale.ENGLISH));
            if (value != null) {
                command = value;
            } else if (games.containsValue(game.name.toLowerCase(Locale.ENGLISH))) {
                command = game.name;
            } else {
                UChat.chat(ChatColor.RED + "Invalid game: \"" + game.name + "\"");
                return;
            }
        }

        if (HytilsConfig.limboPlayCommandHelper && LimboLimiter.inLimbo()) {
            HytilsReborn.INSTANCE.getCommandQueue().queue("/l");
        }
        HytilsReborn.INSTANCE.getCommandQueue().queue("/play " + command);
    }
}
