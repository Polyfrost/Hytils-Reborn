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
import club.sk1er.modcore.ModCoreInstaller;
import club.sk1er.mods.core.util.MinecraftUtils;
import club.sk1er.mods.core.util.Multithreading;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class PlayCommand extends CommandBase {
    private HashMap<String, String> games = new HashMap<>();

    public PlayCommand() {
        getNames();
    }

    private void getNames() {
        Multithreading.runAsync(() -> {
            try {
                String url = "https://gist.githubusercontent.com/PyICoder/f1348916ff1375e87c6821c5402e35e2/raw";
                String content = ModCoreInstaller.fetchString(url);
                Type stringStringMap = new TypeToken<HashMap<String, String>>() {
                }.getType();
                games = new Gson().fromJson(content, stringStringMap);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
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
        boolean isEnabled = HytilitiesConfig.autocompletePlayCommands;
        if (!MinecraftUtils.isHypixel()) {
            Hytilities.INSTANCE.getCommandQueue().queue("/play " + String.join(" ", args));
            return;
        }
        if (args.length != 1) {
            if (isEnabled) {
                Hytilities.INSTANCE.sendMessage("&cSpecify a game");
            }
            return;
        }
        String command = args[0];
        if (isEnabled) {
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
        Hytilities.INSTANCE.getCommandQueue().queue("/play " + command);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return (HytilitiesConfig.autocompletePlayCommands && MinecraftUtils.isHypixel()) ? getListOfStringsMatchingLastWord(args, getListofGames()) : null;
    }

    private String[] getListofGames() {
        return Stream.concat(games.keySet().stream(), games.values().stream()).distinct().toArray(String[]::new);
    }
}
