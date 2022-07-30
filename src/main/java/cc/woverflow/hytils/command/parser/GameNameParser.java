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

package cc.woverflow.hytils.command.parser;

import cc.polyfrost.oneconfig.utils.commands.arguments.ArgumentParser;
import cc.polyfrost.oneconfig.utils.commands.arguments.Arguments;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.woverflow.hytils.config.HytilsConfig;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameNameParser extends ArgumentParser<GameName> {
    private String[] gameCache = null;
    private final Map<String, String> games;
    public GameNameParser(Map<String, String> games) {
        this.games = games;
    }
    @Override
    public @Nullable GameName parse(Arguments arguments) {
        StringBuilder builder = new StringBuilder();
        while (arguments.hasNext()) {
            String arg = arguments.poll();
            builder.append(arg).append(" ");
        }
        return new GameName(builder.toString().trim());
    }

    @Override
    public @Nullable List<String> complete(Arguments arguments, Parameter parameter) {
        if (!HypixelUtils.INSTANCE.isHypixel() || !HytilsConfig.autocompletePlayCommands) return null;
        String game = arguments.poll();
        return Stream.of(getListOfGames()).filter(s -> s.startsWith(game)).collect(Collectors.toList());
    }

    private String[] getListOfGames() {
        if (this.gameCache != null) return this.gameCache;
        return this.gameCache = Stream.concat(games.keySet().stream(), games.values().stream()).distinct().toArray(String[]::new);
    }
}
