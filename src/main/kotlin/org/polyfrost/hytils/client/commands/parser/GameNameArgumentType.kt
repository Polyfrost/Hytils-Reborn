package org.polyfrost.hytils.client.commands.parser

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import java.util.concurrent.CompletableFuture

class GameNameArgumentType(private val games: Map<String, String>) : ArgumentType<GameNameArgumentType.GameName> {
    private val cache: Array<String> by lazy {
        (games.keys + games.values).distinct().toTypedArray()
    }

    override fun parse(reader: StringReader): GameName {
        return GameName(reader.readUnquotedString())
    }

    override fun getExamples(): Collection<String> = cache.take(3)

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        if (!HypixelUtils.isHypixel() || !HytilsRebornConfig.autocompletePlayCommands) {
            return builder.buildFuture()
        }

        for (s in cache) {
            if (s.startsWith(builder.remaining, ignoreCase = true)) {
                builder.suggest(s)
            }
        }

        return builder.buildFuture()
    }

    companion object {
        fun gameName(games: Map<String, String>): GameNameArgumentType {
            return GameNameArgumentType(games)
        }
    }

    data class GameName(val name: String)
}
