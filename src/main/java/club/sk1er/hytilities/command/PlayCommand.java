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

package club.sk1er.hytilities.command;


import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.lobby.limbo.LimboLimiter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PlayCommand extends CommandBase {
    private Map<String, String> games = new HashMap<>();
    private String[] gameCache = null;

    public PlayCommand() {
        this.getNames();
    }

    private void getNames() {
        Multithreading.runAsync(() -> {
            try {
                String url = "https://gist.githubusercontent.com/asbyth/16ab6fcbca18f3f4a14d61d04e7ebeb5/raw";
                String content = WebUtil.fetchString(url);
                Type stringStringMap = new TypeToken<HashMap<String, String>>() {
                }.getType();
                games = new Gson().fromJson(content, stringStringMap);
            } catch (JsonSyntaxException e) {
                Hytilities.INSTANCE.getLogger().error("Failed to fetch /play game list.", e);
            }
        });
    }

    @Override
    public String getCommandName() {
        return "play";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/play [game]";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) {
        boolean autocompletePlayCommands = HytilitiesConfig.autocompletePlayCommands;
        if (!EssentialAPI.getMinecraftUtil().isHypixel()) {
            Hytilities.INSTANCE.getCommandQueue().queue("/play " + String.join(" ", args));
            return;
        }

        if (args.length != 1) {
            if (autocompletePlayCommands) {
                Hytilities.INSTANCE.sendMessage("&cSpecify a game");
            }

            return;
        }

        String command = args[0];
        if (autocompletePlayCommands) {
            String value = games.get(args[0].toLowerCase());
            if (value != null) {
                command = value;
            } else if (games.containsValue(args[0].toLowerCase())) {
                command = args[0];
            } else {
                Hytilities.INSTANCE.sendMessage("&cInvalid game: \"" + args[0] + "\"");
                return;
            }
        }

        if (HytilitiesConfig.limboPlayCommandHelper && LimboLimiter.inLimbo()){
            Hytilities.INSTANCE.getCommandQueue().queue("/l");
        }
        Hytilities.INSTANCE.getCommandQueue().queue("/play " + command);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return (HytilitiesConfig.autocompletePlayCommands && EssentialAPI.getMinecraftUtil().isHypixel()) ? getListOfStringsMatchingLastWord(args, getListOfGames()) : null;
    }

    private String[] getListOfGames() {
        if (this.gameCache != null) return this.gameCache;
        return this.gameCache = Stream.concat(games.keySet().stream(), games.values().stream()).distinct().toArray(String[]::new);
    }
}
