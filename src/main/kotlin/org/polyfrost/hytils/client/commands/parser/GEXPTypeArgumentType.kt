package org.polyfrost.hytils.client.commands.parser

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.Locale
import java.util.concurrent.CompletableFuture

class GEXPTypeArgumentType : ArgumentType<GEXPTypeArgumentType.GEXPType> {
    override fun parse(reader: StringReader): GEXPType {
        val argument = reader.readUnquotedString()
        return runCatching { GEXPType.valueOf(argument.uppercase(Locale.ROOT)) }
            .getOrElse {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                    .create("Invalid GEXP type: $argument")
            }
    }

    override fun getExamples(): Collection<String> = GEXPType.entries.map { it.name.lowercase(Locale.ROOT) }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        for (type in GEXPType.entries) {
            if (type.name.startsWith(builder.remaining, true)) {
                builder.suggest(type.name.lowercase(Locale.ROOT))
            }
        }

        return builder.buildFuture()
    }

    companion object {
        fun gexpType(): GEXPTypeArgumentType {
            return GEXPTypeArgumentType()
        }
    }

    enum class GEXPType { DAILY, WEEKLY }
}
