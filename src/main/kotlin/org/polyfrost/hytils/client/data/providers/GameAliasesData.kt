package org.polyfrost.hytils.client.data.providers

import com.google.gson.JsonElement
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils

object GameAliasesData : DataProvider {
    @Volatile var aliases = emptyMap<String, String>()

    override fun load() {
        val response: JsonElement? = JsonUtils.parseFromUrl("$apiBase/game_aliases.json")
        response?.asJsonObject?.entrySet()?.associate { it.key to it.value.asString }?.let { this.aliases = it }
    }
}
