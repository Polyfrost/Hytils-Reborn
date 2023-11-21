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

package org.polyfrost.hytils.command.parser;

import cc.polyfrost.oneconfig.utils.commands.arguments.ArgumentParser;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import org.polyfrost.hytils.config.HytilsConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.Collections;
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

    @Nullable
    @Override
    public GameName parse(@NotNull String arg) throws Exception {
        return new GameName(arg);
    }

    @Override
    public @NotNull List<String> complete(String game, Parameter parameter) {
        if (!HypixelUtils.INSTANCE.isHypixel() || !HytilsConfig.autocompletePlayCommands) {
            return Collections.emptyList();
        }
        return Stream.of(getListOfGames()).filter(s -> s.startsWith(game)).collect(Collectors.toList());
    }

    private String[] getListOfGames() {
        if (this.gameCache == null) {
            this.gameCache = Stream.concat(games.keySet().stream(), games.values().stream()).distinct().toArray(String[]::new);
        }
        return this.gameCache;
    }
}
