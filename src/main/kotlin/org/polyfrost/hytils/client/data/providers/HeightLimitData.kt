package org.polyfrost.hytils.client.data.providers

import net.hypixel.data.type.GameType
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.utils.v1.JsonUtils
import java.util.*

object HeightLimitData : DataProvider {
    @Volatile var maps = EnumMap<GameType, Map<String, MapEntry>>(GameType::class.java)

    override val apiBase
        get() = "https://data.polyfrost.org/hlm"

    override fun load() {
        val response = JsonUtils.parseFromUrl("$apiBase/trans-rights-are-human-rights.json") // so true

        val maps = EnumMap<GameType, Map<String, MapEntry>>(GameType::class.java)
        response?.asJsonArray?.forEach { element ->
            val gameType = GameType.valueOf(element.asJsonObject.get("gameType").asString)
            val innerMap = maps.getOrPut(gameType) { mutableMapOf() } as MutableMap
            val snakeCaseName = element.asJsonObject.get("name").asString.lowercase(Locale.ROOT).replace(" ", "_")

            val entry = MapEntry(
                minBuild = element.asJsonObject.get("minBuild").asInt,
                maxBuild = element.asJsonObject.get("maxBuild").asInt,
            )

            innerMap[snakeCaseName] = entry
        }
        this.maps = maps
    }

    data class MapEntry(
        val minBuild: Int,
        val maxBuild: Int,
    )
}
