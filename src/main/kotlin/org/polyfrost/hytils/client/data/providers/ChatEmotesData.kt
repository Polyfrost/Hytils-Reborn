package org.polyfrost.hytils.client.data.providers

import com.mojang.serialization.JsonOps
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils

object ChatEmotesData : DataProvider {
    @Volatile var emotes = mapOf<String, List<Component>>()

    override fun load() {
        val response = JsonUtils.parseFromUrl("$apiBase/chat_emotes.json")
        response?.asJsonObject?.entrySet()?.associate { entry ->
            val components = entry.value.asJsonArray.mapNotNull { e ->
                ComponentSerialization.CODEC
                    .decode(JsonOps.INSTANCE, e)
                    .getOrThrow()
                    .first
            }
            entry.key to components
        }?.let { this.emotes = it }
    }
}
