package org.polyfrost.hytils.command.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.polyfrost.hytils.config.HytilsConfig;
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class GameNameArgumentType implements ArgumentType<GameName> {
    private final Map<String, String> games;
    private volatile String[] cache; // lazy, distinct keys+values

    public GameNameArgumentType(Map<String, String> games) {
        this.games = Objects.requireNonNull(games, "games");
    }

    public static GameNameArgumentType gameName(Map<String, String> games) {
        return new GameNameArgumentType(games);
    }

    @Override
    public GameName parse(StringReader reader) throws CommandSyntaxException {
        final String arg = reader.readUnquotedString();
        return new GameName(arg);
    }

    @Override
    public Collection<String> getExamples() {
        String[] list = listOfGames();
        int n = Math.min(3, list.length);
        List<String> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(list[i]);
        return out;
    }

    public <S> SuggestionProvider<S> suggestions() {
        return (CommandContext<S> ctx, SuggestionsBuilder b) -> {
            if (!HypixelUtils.isHypixel() || !HytilsConfig.autocompletePlayCommands) {
                // keep behavior: no suggestions at all
                return CompletableFuture.completedFuture(b.build());
            }
            String remaining = b.getRemaining(); // original was case-sensitive startsWith
            for (String s : listOfGames()) {
                if (s.startsWith(remaining)) {
                    b.suggest(s);
                }
            }
            return CompletableFuture.completedFuture(b.build());
        };
    }

    private String[] listOfGames() {
        String[] local = cache;
        if (local == null) {
            synchronized (this) {
                local = cache;
                if (local == null) {
                    local = Stream.concat(games.keySet().stream(), games.values().stream())
                        .distinct()
                        .toArray(String[]::new);
                    cache = local;
                }
            }
        }
        return local;
    }
}
