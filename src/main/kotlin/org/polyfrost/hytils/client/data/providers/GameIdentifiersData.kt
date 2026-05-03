package org.polyfrost.hytils.client.data.providers

import com.google.gson.JsonElement
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils

object GameIdentifiersData : DataProvider {
    @Volatile var games = emptyMap<String, Map<String, String>>()

    override fun load() {
        val response: JsonElement? = JsonUtils.parseFromUrl("$apiBase/game_identifiers.json")
        response?.asJsonObject?.entrySet()?.associate { entry ->
            entry.key to entry.value.asJsonObject.entrySet().associate { it.key to it.value.asString }
        }?.let { this.games = it }
    }
}
