package org.polyfrost.hytils.command.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class GEXPTypeArgumentType implements ArgumentType<GEXPType> {
    @Override
    public GEXPType parse(@NotNull StringReader reader) throws CommandSyntaxException {
        String arg = reader.readUnquotedString();
        try {
            return GEXPType.valueOf(arg.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Unknown GEXPType: " + arg);
        }
    }

    @Override
    public Collection<String> getExamples() {
        // Brigadier uses this for help / usage output
        return Arrays.asList("guild", "network", "quest");
    }

    public static <S> SuggestionProvider<S> suggestions() {
        return (CommandContext<S> context, SuggestionsBuilder builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ENGLISH);
            for (GEXPType type : GEXPType.values()) {
                String lower = type.name().toLowerCase(Locale.ENGLISH);
                if (lower.startsWith(remaining)) {
                    builder.suggest(lower);
                }
            }
            return CompletableFuture.completedFuture(builder.build());
        };
    }

    public static GEXPTypeArgumentType gexpType() {
        return new GEXPTypeArgumentType();
    }
}
