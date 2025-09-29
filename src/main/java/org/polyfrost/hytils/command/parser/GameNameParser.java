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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;
import org.polyfrost.hytils.config.HytilsConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameNameParser implements ArgumentType<GameName> {
    private String[] gameCache = null;
    private final Map<String, String> games;

    public GameNameParser(Map<String, String> games) {
        this.games = games;
    }

    @Override
    public GameName parse(StringReader arg) {
        return new GameName(arg.getRead());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (!HypixelUtils.isHypixel() || !HytilsConfig.autocompletePlayCommands) {
            return ArgumentType.super.listSuggestions(context, builder);
        }
        String remaining = builder.getRemaining().toLowerCase();
        for (String game : getListOfGames()) {
            if (game.toLowerCase().startsWith(remaining)) {
                builder.suggest(game);
            }
        }
        return builder.buildFuture();
    }

    private String[] getListOfGames() {
        if (this.gameCache == null) {
            this.gameCache = Stream.concat(games.keySet().stream(), games.values().stream()).distinct().toArray(String[]::new);
        }
        return this.gameCache;
    }
}
