package org.polyfrost.hytils.command.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WinstreakTypeArgumentType implements ArgumentType<WinstreakType> {
    private static final List<String> DEFAULT_COMPLETIONS =
        Arrays.stream(WinstreakType.values())
            .map(type -> type.name().toLowerCase(Locale.ENGLISH))
            .collect(Collectors.toList());

    @Override
    public WinstreakType parse(StringReader reader) throws CommandSyntaxException {
        String arg = reader.readUnquotedString();
        try {
            return WinstreakType.valueOf(arg.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                .create("Unknown WinstreakType: " + arg);
        }
    }

    @Override
    public Collection<String> getExamples() {
        return DEFAULT_COMPLETIONS.subList(0, Math.min(3, DEFAULT_COMPLETIONS.size()));
    }

    public static <S> SuggestionProvider<S> suggestions() {
        return (CommandContext<S> context, SuggestionsBuilder builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ENGLISH);

            if (remaining.isEmpty()) {
                for (String comp : DEFAULT_COMPLETIONS) {
                    builder.suggest(comp);
                }
            } else {
                for (WinstreakType type : WinstreakType.values()) {
                    String lower = type.name().toLowerCase(Locale.ENGLISH);
                    if (lower.startsWith(remaining)) {
                        builder.suggest(lower);
                    }
                }
            }
            return CompletableFuture.completedFuture(builder.build());
        };
    }

    public static WinstreakTypeArgumentType winstreakType() {
        return new WinstreakTypeArgumentType();
    }
}
